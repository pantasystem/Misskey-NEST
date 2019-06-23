package org.panta.misskeynest.view.follow_follower

import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.filter.FollowFollowerFilter
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.FollowFollowerContract
import org.panta.misskeynest.repository.IItemRepository
import org.panta.misskeynest.repository.remote.UserRepository
import org.panta.misskeynest.interactor.PagingController
import org.panta.misskeynest.viewdata.FollowViewData

class FollowFollowerPresenter(private val mView: FollowFollowerContract.View, mTimeline: IItemRepository<FollowViewData>, private val info: ConnectionProperty)
    : FollowFollowerContract.Presenter, ErrorCallBackListener{

    private val pagingController =
        PagingController(mTimeline, this, FollowFollowerFilter())
    override fun getItems() {
        pagingController.init {
            mView.showItems(it)
            mView.stopRefreshing()
        }
    }

    override fun getNewItems() {
        pagingController.getNewItems {
            mView.showNewItems(it)
            mView.stopRefreshing()
        }
    }

    override fun getOldItems() {
        pagingController.getOldItems {
            mView.showOldItems(it)
            mView.stopRefreshing()
        }
    }

    override fun start() {

    }

    override fun onFollowUnFollowButtonClicked(item: FollowViewData){
        if(item.following?.id == null){
            UserRepository(domain = info.domain, authKey = info.i).followUser(item.follower?.id!!){
                if(it){
                    mView.updateItem(item.copy(following = item.follower))
                }
            }
        }else{
            UserRepository(domain = info.domain, authKey = info.i).unFollowUser(item.following.id){
                if(it){
                    mView.removeItem(item)
                }
            }
        }
    }

    override fun callBack(e: Exception) {
        mView.showError(e)
        mView.stopRefreshing()
    }
}