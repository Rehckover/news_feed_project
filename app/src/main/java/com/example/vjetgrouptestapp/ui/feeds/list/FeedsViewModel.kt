package com.example.vjetgrouptestapp.ui.feeds.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.vjetgrouptestapp.base.utils.CombinedLiveData
import com.example.vjetgrouptestapp.base.db.LocalRepository
import com.example.vjetgrouptestapp.base.remote.RemoteRepository
import com.example.vjetgrouptestapp.base.remote.RemoteSettings
import com.example.vjetgrouptestapp.base.remote.models.FeedModel
import com.example.vjetgrouptestapp.base.remote.models.SourceModel
import com.example.vjetgrouptestapp.base.utils.DATE_FORMAT
import com.example.vjetgrouptestapp.ui.feeds.list.FeedPagingFactory
import digital.cvlt.app.core.platform.BaseViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_SOURCE = "reddit-r-all"
const val TAG = "FeedsViewModel"

class FeedsViewModel @ViewModelInject constructor(
    val remoteRepository: RemoteRepository,
    val localRepository: LocalRepository,
    feedPagingFactory: FeedPagingFactory
) : BaseViewModel() {

    var tempSearchOptions: SearchOptions? = null
    var searchOptions = MutableLiveData<SearchOptions>()
    var sources = MutableLiveData<List<SourceModel>>()
    var pagedFeeds =
        CombinedLiveData(searchOptions) {
            feedPagingFactory.getFeedPagingList(it, viewModelScope)
        }

    fun updateSearchOptions(forceReset: Boolean = false) {
        createOrResetSearchOptions(forceReset)
        searchOptions.value = tempSearchOptions
    }

    fun getSources() {
        try {
            viewModelScope.launch {
                val sourceResponse = remoteRepository
                    .getSources(RemoteSettings.API_KEY)
                    .await()
                sources.value = sourceResponse.sources
                if (sourceResponse.isSuccessful()) {
                    updateSearchOptions()
                }
            }
        } catch (e: Exception) {
            handleException(e)
        }

    }

    fun getDefaultSearchOptions() = SearchOptions(
        source = sources.value?.firstOrNull()?.id ?: DEFAULT_SOURCE,
        dateFrom = getInitialDateFrom(),
        dateTo = getInitialDateTo()
    )

    fun createOrResetSearchOptions(forceReset: Boolean = false) {
        if (tempSearchOptions == null || forceReset)
            tempSearchOptions = getDefaultSearchOptions()
    }

    fun handleFavClick(feedModel: FeedModel?) {
        if (feedModel == null) return
        viewModelScope.launch {
            Log.d(TAG, "saveFeedModel: ")
            try {
                if (feedModel.isFavourite)
                    localRepository.saveFeed(feedModel)
                else
                    localRepository.deleteFeed(feedModel)
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun handleException(e: Exception) {
        Log.d(TAG, "handleException: ${e.localizedMessage}")
        e.printStackTrace()
    }

    @SuppressLint("SimpleDateFormat")
    fun getInitialDateFrom(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        return SimpleDateFormat(DATE_FORMAT).format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getInitialDateTo(): String {
        val calendar = Calendar.getInstance()
        return SimpleDateFormat(DATE_FORMAT).format(calendar.time)
    }
}