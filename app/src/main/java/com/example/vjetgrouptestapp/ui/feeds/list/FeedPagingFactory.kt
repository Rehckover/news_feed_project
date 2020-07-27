package com.example.vjetgrouptestapp.ui.feeds.list

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.vjetgrouptestapp.base.db.LocalRepository
import com.example.vjetgrouptestapp.base.remote.RemoteRepository
import com.example.vjetgrouptestapp.base.remote.models.FeedModel
import com.example.vjetgrouptestapp.ui.feeds.list.adapter.FeedDataSource
import kotlinx.coroutines.CoroutineScope

private const val DEFAULT_PAGE_SIZE = 30
private const val PREFETCH_DISTANCE = DEFAULT_PAGE_SIZE / 2
private const val INITIAL_LOAD_SIZE =
    DEFAULT_PAGE_SIZE

class FeedPagingFactory(val remoteRepository : RemoteRepository,val localRepository : LocalRepository) {

    fun getFeedPagingList(
        searchOptions: SearchOptions?,
        scope: CoroutineScope
    ): LiveData<PagedList<FeedModel>> {
        val dataSourceFactory = object : DataSource.Factory<Int, FeedModel>() {
            override fun create(): DataSource<Int, FeedModel> {
                return FeedDataSource(
                    searchOptions = searchOptions,
                    remoteRepository = remoteRepository,
                    localRepository = localRepository,
                    scope = scope
                )
            }
        }
        return LivePagedListBuilder(
            dataSourceFactory,
            getPageConfig()
        ).build()
    }

    fun getLocalPagedList(scope: CoroutineScope): LiveData<PagedList<FeedModel>> {
        val dataSourceFactory = localRepository.getFeeds()
        return LivePagedListBuilder(
            dataSourceFactory,
            getPageConfig()
        ).build()
    }

    private fun getPageConfig(): PagedList.Config =
        PagedList.Config.Builder()
            .setPageSize(DEFAULT_PAGE_SIZE)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .setEnablePlaceholders(false)
            .build()

}