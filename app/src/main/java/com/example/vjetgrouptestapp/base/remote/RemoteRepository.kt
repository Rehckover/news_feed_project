package com.example.vjetgrouptestapp.base.remote

import com.example.corutinepro.com.car.forme.repository.api.Api
import com.example.vjetgrouptestapp.base.remote.models.FeedResponse
import com.example.vjetgrouptestapp.base.remote.models.SourcesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit

class RemoteRepository(retrofit: Retrofit) : Api {

    val api = retrofit.create(Api::class.java)

    override fun getFeeds(
        apiKey: String,
        pageSize: Int,
        page: Int,
        sources: String?,
        dateFrom: String?,
        dateTo: String?,
        sortBy: String?
    ): Deferred<FeedResponse> =
        api.getFeeds(apiKey, pageSize, page, sources, dateFrom, dateTo, sortBy)

    override fun getSources(apiKey: String): Deferred<SourcesResponse> =
        api.getSources(apiKey)


}