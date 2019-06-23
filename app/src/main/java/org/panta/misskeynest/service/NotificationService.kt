package org.panta.misskeynest.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.panta.misskeynest.constant.NotificationType
import org.panta.misskeynest.entity.NotificationProperty
import org.panta.misskeynest.filter.NotificationFilter
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.repository.remote.NotificationRepository
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.storage.SharedPreferenceOperator
import org.panta.misskeynest.interactor.PagingController
import org.panta.misskeynest.MainActivity
import org.panta.misskeynest.viewdata.NotificationViewData



class NotificationService : Service() {

    private lateinit var notificationRepository: NotificationRepository
    private lateinit var pagingController: PagingController<NotificationProperty, NotificationViewData>

    private val notificationChannelId = "Misskey for Adnroid Notification"

    private var isActive = true

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        //~init
        val conProperty = PersonalRepository(
            SharedPreferenceOperator(
                applicationContext
            )
        ).getConnectionInfo()
        if(conProperty == null){
            Log.d("NotificationService", "connectionInfo不明のため停止します")
            this.stopSelf()
            return
        }
        notificationRepository =
            NotificationRepository(conProperty.domain, conProperty.i)

        pagingController =
            PagingController(notificationRepository, object : ErrorCallBackListener {
                override fun callBack(e: Exception) {
                    Log.w("NotificationService", "エラー発生", e)
                }
            }, NotificationFilter())
        //init~

        GlobalScope.launch{

            pagingController.init {
                watchDogNotification(10000)
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        isActive = false
    }

    private fun watchDogNotification(sleepMillSeconds: Long){
        if(sleepMillSeconds.toString().length < 4) throw IllegalArgumentException("watchDogNotificationsError　1000ミリ秒未満を指定することはできません。")
        GlobalScope.launch{
            while(isActive){
                pagingController.getNewItems {
                    val notReadNotifications = it.filter{ inner ->
                        ! inner.notificationProperty.isRead
                    }

                    notReadNotifications.forEach{inner ->
                        Log.d("NotificationService", "未読の通知 ${inner.notificationProperty}")
                        showNotificationCompat(inner)

                    }
                }
                delay(sleepMillSeconds)
            }
        }
    }

    private fun showNotificationCompat(notificationViewData: NotificationViewData){

        try{
            val type = NotificationType.getEnumFromString(notificationViewData.notificationProperty.type)
            val typeMessage = when(type){
                NotificationType.REACTION ->{
                    notificationViewData.notificationProperty.reaction.toString()
                }
                NotificationType.RENOTE -> "リノート"
                NotificationType.FOLLOW -> "フォローされました"
                NotificationType.MENTION -> "あなたについて投稿"
                NotificationType.QUOTE -> "引用リノート"
                NotificationType.REPLY -> "リプライ"
                NotificationType.RECEIVE_FOLLOW_REQUEST -> "フォローリクエスト"
                NotificationType.POLL_VOTE -> "投票"
            }
            val userName = notificationViewData.notificationProperty.user.name
            val title = "$userName さんが$typeMessage しました"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                if(notificationManager.getNotificationChannel(notificationChannelId) == null){
                    val channel = NotificationChannel(notificationChannelId, "Misskey", NotificationManager.IMPORTANCE_HIGH)
                    channel.apply{
                        description = "詳細"
                    }
                    notificationManager.createNotificationChannel(channel)
                }
            }

            val notification = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Notification.Builder(applicationContext, notificationChannelId)
            }else{
                Notification.Builder(applicationContext)
            }

            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra(MainActivity.SHOW_FRAGMENT_TAG, MainActivity.NOTIFICATION)

            val contentIntent = PendingIntent.getActivity(applicationContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT)

            val build = notification.apply {
                setSmallIcon(org.panta.misskeynest.R.drawable.misskey_icon)
                setContentTitle(title)
                setContentIntent(contentIntent)
            }.build()


            val nm = NotificationManagerCompat.from(this)
            nm.notify(1, build)
        }catch(e: Exception){
            Log.w("NotificationService", "通知表示中にエラー発生", e)
        }

    }
}
