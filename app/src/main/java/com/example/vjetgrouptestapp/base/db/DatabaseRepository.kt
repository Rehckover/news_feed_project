package com.example.vjetgrouptestapp.base.db

import androidx.paging.DataSource
import com.example.vjetgrouptestapp.base.remote.models.FeedModel


interface DatabaseRepository {

    suspend fun saveFeed(feedModel: FeedModel)

    suspend fun deleteFeed(feedModel: FeedModel)

    fun getFeeds(): DataSource.Factory<Int, FeedModel>?

    fun getFeedsIds(): List<String>
}
