package com.example.corutinepro.com.car.forme.repository.api

import com.example.vjetgrouptestapp.base.remote.models.FeedResponse
import com.example.vjetgrouptestapp.base.remote.models.SourceModel
import com.example.vjetgrouptestapp.base.remote.models.SourcesResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("everything")
    fun getFeeds(
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("sources") sources: String?,
        @Query("from") dateFrom: String?,
        @Query("to") dateTo: String?,
        @Query("sortBy") sortBy: String?
    ): Deferred<FeedResponse>

    @GET("sources")
    fun getSources(
        @Query("apiKey") apiKey: String
    ): Deferred<SourcesResponse>
}