package com.example.vjetgrouptestapp.base.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vjetgrouptestapp.base.remote.models.FeedModel
import com.example.vjetgrouptestapp.base.remote.models.SourceModel

@Database(
    entities = [
        FeedModel::class,
        SourceModel::class
    ],
    version = DbSettings.DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseRepository(): DatabaseDao
}