package com.example.vjetgrouptestapp.base.remote.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vjetgrouptestapp.base.remote.RemoteSettings

data class SourcesResponse(
    val sources: List<SourceModel>,
    val status: String
) {
    fun isSuccessful() = status == RemoteSettings.STATUS_OK
}

@Entity
data class SourceModel(
    @PrimaryKey
    val id: String,
    val name: String
)