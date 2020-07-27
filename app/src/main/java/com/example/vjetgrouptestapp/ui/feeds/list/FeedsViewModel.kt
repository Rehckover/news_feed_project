package com.example.vjetgrouptestapp.ui.feeds.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.vjetgrouptestapp.base.db.LocalRepository
import com.example.vjetgrouptestapp.base.remote.RemoteRepository
import com.example.vjetgrouptestapp.base.remote.RemoteSettings
import com.example.vjetgrouptestapp.base.remote.models.FeedModel
import com.example.vjetgrouptestapp.base.remote.models.Result
import com.example.vjetgrouptestapp.base.remote.models.SourceModel
import com.example.vjetgrouptestapp.base.utils.CombinedLiveData
import com.example.vjetgrouptestapp.base.utils.DATE_FORMAT
import com.example.vjetgrouptestapp.base.arch.BaseViewModel
import com.example.vjetgrouptestapp.base.remote.models.ErrorEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_SOURCE = "reddit-r-all"
const val TAG = "FeedsViewModel"

class FeedsViewModel @ViewModelInject constructor(
    private val remoteRepository: RemoteRepository,
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
        if (sources.value != null) {
            updateSearchOptions()
            return
        }
        viewModelScope.launch {
            val sourcesResult = remoteRepository
                .getSources(RemoteSettings.API_KEY)
            when (sourcesResult) {
                is Result.Success -> {
                    this@FeedsViewModel.sources.value = sourcesResult.data
                    updateSearchOptions()
                }
                is Result.Error -> {
                    handleErrors(sourcesResult)
                }
            }
        }
    }

    private fun getDefaultSearchOptions() = SearchOptions(
        source = sources.value?.firstOrNull()?.id ?: DEFAULT_SOURCE,
        dateFrom = getInitialDateFrom(),
        dateTo = getInitialDateTo()
    )

    private fun createOrResetSearchOptions(forceReset: Boolean = false) {
        if (tempSearchOptions == null || forceReset)
            tempSearchOptions = getDefaultSearchOptions()
    }

    fun handleFavClick(feedModel: FeedModel?) {
        if (feedModel == null) return
        viewModelScope.launch {
            try {
                if (feedModel.isFavourite)
                    localRepository.saveFeed(feedModel)
                else
                    localRepository.deleteFeed(feedModel)
            } catch (e: Exception) {
                handleErrors(Result.Error(ErrorEntity.DbException(e.localizedMessage)))
            }
        }
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