package com.example.vjetgrouptestapp.ui.feeds.list.adapter

import androidx.lifecycle.MutableLiveData
import com.example.vjetgrouptestapp.base.arch.BaseItemState
import com.example.vjetgrouptestapp.base.extensions.falseIfNull
import com.example.vjetgrouptestapp.base.extensions.reformatDate
import com.example.vjetgrouptestapp.base.remote.models.FeedModel

class FeedItemState(
    private val item: FeedModel?,
    private val listener: FeedsRvAdapter.FeedsAdapterListener<FeedModel>
): BaseItemState() {

    val title = MutableLiveData<String>()
    val image = MutableLiveData<String>()
    val source = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val isFavourite = MutableLiveData<Boolean>()

    init {
        title.value = item?.title
        image.value = item?.urlToImage
        date.value = item?.publishedAt?.reformatDate()
        source.value = item?.source?.name
        isFavourite.value  = item?.isFavourite
    }

    fun onFavClick() {
        item?.isFavourite = !item?.isFavourite.falseIfNull()
        isFavourite.value  = item?.isFavourite
        listener.onFavClick(item)
    }

    fun onDownloadClick(){
        listener.onDownLoadClick(item)
    }

    fun onShareClick(){
        listener.onShareClick(item)
    }

}