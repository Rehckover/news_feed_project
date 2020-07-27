package com.example.vjetgrouptestapp.ui.feeds.favourite

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.vjetgrouptestapp.R
import com.example.vjetgrouptestapp.base.arch.BaseFragment
import com.example.vjetgrouptestapp.base.remote.models.FeedModel
import com.example.vjetgrouptestapp.base.utils.DownloadHelper
import com.example.vjetgrouptestapp.base.utils.ShareHelper
import com.example.vjetgrouptestapp.ui.feeds.list.adapter.FeedsRvAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feeds_favourite.*

@AndroidEntryPoint
class FavouriteFeedsFragment : BaseFragment<FavouriteFeedsViewModel>() {

    lateinit var feedsRvAdapter: FeedsRvAdapter

    override fun viewModelClass() = FavouriteFeedsViewModel::class.java

    override fun layoutResId() = R.layout.fragment_feeds_favourite

    override fun subscribeToEvents() {
        super.subscribeToEvents()
        viewModel.favFeeds.observe(viewLifecycleOwner, Observer {
            feedsRvAdapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFeedFavAdapter()
    }

    fun setupFeedFavAdapter() {
        feedsRvAdapter =
            FeedsRvAdapter(
                listener = object :
                    FeedsRvAdapter.FeedsAdapterListener<FeedModel> {
                    override fun onFavClick(feedModel: FeedModel?) {
                        viewModel.handleFavClick(feedModel)
                    }

                    override fun onShareClick(feedModel: FeedModel?) {
                        ShareHelper.shareFeed(requireContext(), feedModel?.title, feedModel?.url)
                    }

                    override fun onDownLoadClick(feedModel: FeedModel?) {
                        DownloadHelper.downloadImageByUrl(requireContext(),feedModel?.url)
                    }
                })
        rv_feeds.adapter = feedsRvAdapter
    }


}