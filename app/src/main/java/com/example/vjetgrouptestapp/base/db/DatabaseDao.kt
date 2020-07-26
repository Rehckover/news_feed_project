package com.example.vjetgrouptestapp.base.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.vjetgrouptestapp.base.remote.models.FeedModel

@Dao
abstract class DatabaseDao : DatabaseRepository {

    @Insert(onConflict = REPLACE)
    abstract override suspend fun saveFeed(feedModel: FeedModel)

    @Delete
    abstract override suspend fun deleteFeed(feedModel: FeedModel)

    @Query("SELECT * FROM feeds")
    abstract override fun getFeeds(): DataSource.Factory<Int, FeedModel>

    @Query("SELECT title FROM feeds")
    abstract override fun getFeedsIds(): List<String>

}