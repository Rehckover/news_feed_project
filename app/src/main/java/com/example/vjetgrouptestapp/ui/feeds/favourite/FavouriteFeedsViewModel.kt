package com.example.vjetgrouptestapp.ui.feeds.favourite

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.example.vjetgrouptestapp.base.arch.BaseViewModel
import com.example.vjetgrouptestapp.base.db.LocalRepository
import com.example.vjetgrouptestapp.base.remote.models.ErrorEntity
import com.example.vjetgrouptestapp.base.remote.models.FeedModel
import com.example.vjetgrouptestapp.base.remote.models.Result
import com.example.vjetgrouptestapp.ui.feeds.list.FeedPagingFactory
import com.example.vjetgrouptestapp.ui.feeds.list.TAG
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavouriteFeedsViewModel @ViewModelInject constructor(
    val localRepository: LocalRepository,
    feedPagingFactory: FeedPagingFactory
) : BaseViewModel() {

    val favFeeds = feedPagingFactory.getLocalPagedList(GlobalScope)

    fun handleFavClick(feedModel: FeedModel?) {
        if (feedModel == null) return
        viewModelScope.launch {
            try {
                if (feedModel.isFavourite)
                    localRepository.saveFeed(feedModel)
                else
                    localRepository.deleteFeed(feedModel)
            } catch (e: Exception) {
                handleErrors(Result.Error(ErrorEntity.DbException(e.localizedMessage)))
            }
        }
    }
}