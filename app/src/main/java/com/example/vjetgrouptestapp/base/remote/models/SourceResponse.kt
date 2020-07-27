package com.example.vjetgrouptestapp.base.remote.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class SourcesResponse(
    val sources: List<SourceModel>,
    val status: String
)

@Entity
data class SourceModel(
    @PrimaryKey
    val id: String,
    val name: String
)