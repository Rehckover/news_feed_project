package com.example.vjetgrouptestapp.ui.feeds.list.adapter

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.vjetgrouptestapp.base.db.LocalRepository
import com.example.vjetgrouptestapp.base.remote.RemoteRepository
import com.example.vjetgrouptestapp.base.remote.RemoteSettings
import com.example.vjetgrouptestapp.base.remote.models.FeedModel
import com.example.vjetgrouptestapp.ui.feeds.list.SearchOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val TAG = "FeedRemoteDataSource"
const val INITIAL_PAGE_KEY = 1

class FeedDataSource(
    private val searchOptions: SearchOptions?,
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, FeedModel>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, FeedModel>
    ) {
        Log.d(TAG, "loadInitial: size ${params.requestedLoadSize}")
        scope.launch {
            try {
                val dbListOfFeedsIds = localRepository.getFeedsIds()
                val response = remoteRepository.getFeeds(
                    apiKey = RemoteSettings.API_KEY,
                    pageSize = params.requestedLoadSize,
                    page = INITIAL_PAGE_KEY,
                    sources = searchOptions?.source,
                    dateTo = searchOptions?.dateTo,
                    dateFrom = searchOptions?.dateFrom,
                    sortBy = searchOptions?.sortBy?.sortValue
                ).await()
                attachFavouriteStateFromDb(dbListOfFeedsIds, response.articles)
                when {
                    response.isSuccessful() -> {
                        val listing = response.articles
                        callback.onResult(
                            listing ?: listOf(), null,
                            INITIAL_PAGE_KEY.plus(1)
                        )
                    }
                }

            } catch (exception: Exception) {

            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FeedModel>) {
        Log.d(TAG, "loadAfter: size ${params.requestedLoadSize} params ${params.key}")
        scope.launch {
            try {
                val dbListOfFeedsIds = localRepository.getFeedsIds()
                val response = remoteRepository.getFeeds(
                    apiKey = RemoteSettings.API_KEY,
                    pageSize = params.requestedLoadSize,
                    page = params.key,
                    sources = searchOptions?.source,
                    dateTo = searchOptions?.dateTo,
                    dateFrom = searchOptions?.dateFrom,
                    sortBy = searchOptions?.sortBy?.sortValue
                ).await()
                attachFavouriteStateFromDb(dbListOfFeedsIds, response.articles)
                when {
                    response.isSuccessful() -> {
                        val listing = response.articles
                        callback.onResult(listing, params.key.plus(1))
                    }
                }

            } catch (exception: Exception) {

            }
        }
    }

    fun attachFavouriteStateFromDb(listOfIds: List<String>, listOfFeedModel: List<FeedModel?>?) {
        listOfFeedModel?.forEach { feedModel ->
            if (listOfIds.any { it == feedModel?.title })
                feedModel?.isFavourite = true
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FeedModel>) {

    }

}