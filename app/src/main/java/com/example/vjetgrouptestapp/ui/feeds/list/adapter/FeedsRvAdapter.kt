package com.example.vjetgrouptestapp.ui.feeds.list.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.example.vjetgrouptestapp.R
import com.example.vjetgrouptestapp.base.arch.BaseListAdapter
import com.example.vjetgrouptestapp.base.remote.models.FeedModel


class FeedsRvAdapter(
    @LayoutRes private val itemRes: Int = R.layout.item_feed,
    listener: FeedsAdapterListener<FeedModel>
) : BaseListAdapter<FeedItemState, FeedModel>(DIFF_CALLBACK, listener) {

    override fun itemState(
        item: FeedModel,
        listener: BaseListAdapter.Listener<FeedModel>,
        position: Int
    ) = FeedItemState(item, listener as FeedsAdapterListener<FeedModel>)

    override fun layoutResId(viewType: Int) = itemRes

    interface FeedsAdapterListener<I> : Listener<I> {
        fun onFavClick(feedModel: FeedModel?) {}
        fun onShareClick(feedModel: FeedModel?) {}
        fun onDownLoadClick(feedModel: FeedModel?) {}
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FeedModel>() {
            override fun areItemsTheSame(oldItem: FeedModel, newItem: FeedModel): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: FeedModel, newItem: FeedModel): Boolean =
                oldItem == newItem
        }
    }
}