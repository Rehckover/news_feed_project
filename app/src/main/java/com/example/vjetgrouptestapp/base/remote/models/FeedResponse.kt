package com.example.vjetgrouptestapp.base.remote.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vjetgrouptestapp.base.remote.RemoteSettings

data class FeedResponse(
    val articles: List<FeedModel>,
    val status: String,
    val totalResults: Int
) {
    fun isSuccessful() = status == RemoteSettings.STATUS_OK
}

@Entity(tableName = "feeds")
data class FeedModel(
    @PrimaryKey
    val title: String,
    val description: String,
    val publishedAt: String,
    @Embedded
    val source:SourceModel,
    val url: String,
    val urlToImage: String,
    var isFavourite: Boolean
)
