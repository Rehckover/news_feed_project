package com.example.vjetgrouptestapp.base.remote.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class FeedResponse(
    val articles: List<FeedModel>,
    val status: String,
    val totalResults: Int
)

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
