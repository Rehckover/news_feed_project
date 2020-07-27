package com.example.vjetgrouptestapp.base.remote

import com.example.vjetgrouptestapp.base.arch.GeneralErrorHandle
import com.example.vjetgrouptestapp.base.remote.models.FeedModel
import com.example.vjetgrouptestapp.base.remote.models.Result
import com.example.vjetgrouptestapp.base.remote.models.SourceModel
import retrofit2.Retrofit

class RemoteRepository(
    retrofit: Retrofit,
    private val generalErrorHandle: GeneralErrorHandle
) {

    private val api: Api = retrofit.create(Api::class.java)

    suspend fun getFeeds(
        apiKey: String,
        pageSize: Int,
        page: Int,
        sources: String?,
        dateFrom: String?,
        dateTo: String?,
        sortBy: String?
    ): Result<List<FeedModel>> = try {
        val feedResponse = api.getFeeds(
            apiKey = apiKey,
            pageSize = pageSize,
            page = page,
            sources = sources,
            dateFrom = dateFrom,
            dateTo = dateTo,
            sortBy = sortBy
        ).await()
        Result.Success(feedResponse.articles)
    } catch (throwable: Throwable) {
        Result.Error(generalErrorHandle.getError(throwable))
    }

    suspend fun getSources(apiKey: String): Result<List<SourceModel>> = try {
        val sourcesResponse = api.getSources(
            apiKey = apiKey
        ).await()
        Result.Success(sourcesResponse.sources)
    } catch (throwable: Throwable) {
        Result.Error(generalErrorHandle.getError(throwable))
    }


}