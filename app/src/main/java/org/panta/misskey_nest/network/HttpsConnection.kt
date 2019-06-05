package org.panta.misskey_nest.network

import java.io.BufferedInputStream
import java.io.InputStream
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection

@Deprecated("エラー内容がわかりにくい") class HttpsConnection  {
    fun get(url: URL): InputStream {

        return try{
            val con = (url.openConnection() as HttpsURLConnection).apply{
                requestMethod = "GET"

                connect()

            }
            BufferedInputStream(con.inputStream)

        }catch (e: Exception){
            throw e
        }
    }

    fun post(url: URL, value: String): InputStream {
        return try{
            val con = (url.openConnection() as HttpsURLConnection).apply{
                requestMethod = "POST"
                instanceFollowRedirects = true
                doOutput = true
                connectTimeout = 3000

                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                val os = this.outputStream
                val ps = PrintStream(os)
                ps.print(value)
                ps.close()


            }
            BufferedInputStream(con.inputStream)

        }catch(e : Exception){
            throw e
        }
    }
}