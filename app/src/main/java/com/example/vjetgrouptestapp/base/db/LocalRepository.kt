package com.example.vjetgrouptestapp.base.db

import androidx.paging.DataSource
import com.example.vjetgrouptestapp.base.remote.models.FeedModel


class LocalRepository(private val databaseDao: DatabaseDao) : DatabaseRepository {

    override suspend fun saveFeed(feedModel: FeedModel) =
        databaseDao.saveFeed(feedModel)

    override suspend fun deleteFeed(feedModel: FeedModel) =
        databaseDao.deleteFeed(feedModel)

    override fun getFeeds(): DataSource.Factory<Int, FeedModel> =
        databaseDao.getFeeds()

    override fun getFeedsIds(): List<String> =
        databaseDao.getFeedsIds()


}