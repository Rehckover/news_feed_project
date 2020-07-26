package com.example.vjetgrouptestapp.ui.feeds.list

import android.animation.ValueAnimator
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vjetgrouptestapp.R
import com.example.vjetgrouptestapp.base.arch.BaseFragment
import com.example.vjetgrouptestapp.base.extensions.falseIfNull
import com.example.vjetgrouptestapp.base.extensions.toPx
import com.example.vjetgrouptestapp.base.remote.models.FeedModel
import com.example.vjetgrouptestapp.base.remote.models.SourceModel
import com.example.vjetgrouptestapp.base.utils.DATE_FORMAT
import com.example.vjetgrouptestapp.base.utils.DialogHelper
import com.example.vjetgrouptestapp.base.utils.ShareHelper
import com.example.vjetgrouptestapp.ui.feeds.list.adapter.FeedsRvAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feeds_list.*
import java.text.SimpleDateFormat
import java.util.*

const val MIN_HEIGHT = 50
const val MAX_HEIGHT = 280
const val WIDTH = 100

@AndroidEntryPoint
class FeedsFragment : BaseFragment<FeedsViewModel>() {

    lateinit var feedsRvAdapter: FeedsRvAdapter

    override fun layoutResId(): Int = R.layout.fragment_feeds_list

    override fun viewModelClass() = FeedsViewModel::class.java

    override fun subscribeToEvents() {
        super.subscribeToEvents()
        viewModel.pagedFeeds.observe(viewLifecycleOwner, Observer {
            feedsRvAdapter.submitList(it)
        })
        viewModel.sources.observe(viewLifecycleOwner, Observer {
            setupSourceAdapter(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearch()
        setupFavBtn()
        setupSortBy()
        setupDates()
        setupApplyAndClearBtns()
        setupFeedsAdapter()
        subscribeToEvents()
        setupInitialRequest()
    }

    fun setupInitialRequest() {
        if (viewModel.tempSearchOptions != null) {
            resumeFilterState()
        } else {
            viewModel.getSources()
        }
    }

    fun setupFeedsAdapter() {
        feedsRvAdapter =
            FeedsRvAdapter(
                listener = object :
                    FeedsRvAdapter.FeedsAdapterListener<FeedModel> {
                    override fun onFavClick(feedModel: FeedModel?) {
                        viewModel.handleFavClick(feedModel)
                    }

                    override fun onShareClick(feedModel: FeedModel?) {
                        ShareHelper.shareFeed(requireContext(),feedModel?.title,feedModel?.url)
                    }

                    override fun onDownLoadClick(feedModel: FeedModel?) {

                    }
                })
        rv_feeds.adapter = feedsRvAdapter
    }

    fun setupApplyAndClearBtns() {
        btn_apply.setOnClickListener {
            viewModel.updateSearchOptions()
        }
        btn_reset.setOnClickListener {
            resetFilter()
            viewModel.updateSearchOptions(forceReset = true)
        }
    }

    fun setupSourceAdapter(listOfSources: List<SourceModel>) {
        sources_spinner.minValue = 0
        sources_spinner.maxValue = listOfSources.size - 1
        sources_spinner.displayedValues = listOfSources.map { it.name }.toTypedArray()
        sources_spinner.setOnValueChangedListener { numberPicker, oldValue, newValue ->
            viewModel.tempSearchOptions?.source = listOfSources[newValue].id
        }
    }

    fun setupSearch() {
        btn_search.isSelected = false
        btn_search.setOnClickListener {
            btn_search.isSelected = !btn_search.isSelected
            if (btn_search.isSelected) {
                startExpandAnimation(search_bar)
            } else {
                startCollapseAnimation(search_bar)
            }
        }
    }

    fun setupFavBtn() {
        btn_fav.setOnClickListener {
            val action = FeedsFragmentDirections.actionFeedsListFragmentToFavouriteFeedsFragment()
            findNavController().navigate(action)
        }
    }

    fun setupDates() {
        tv_date_from.text = viewModel.getInitialDateFrom()
        tv_date_to.text = viewModel.getInitialDateTo()
        setupDateListener(tv_date_from) {
            viewModel.tempSearchOptions?.dateFrom = it
        }
        setupDateListener(tv_date_to) {
            viewModel.tempSearchOptions?.dateTo = it
        }
    }

    fun setupDateListener(dateView: AppCompatTextView, block: (String) -> (Unit)) {
        dateView.setOnClickListener {
            DialogHelper.setDate(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, day)
                    val date = SimpleDateFormat(DATE_FORMAT).format(calendar.time)
                    dateView.text = date
                    block.invoke(date)
                }
            )
        }
    }

    fun setupSortBy() {
        btn_sort_date.isSelected = true
        btn_sort_date.setOnClickListener {
            if (it?.isSelected.falseIfNull()) return@setOnClickListener
            it?.isSelected = !it.isSelected
            btn_sort_popularity?.isSelected = false
            viewModel.tempSearchOptions?.sortBy = SortByType.DATE
        }
        btn_sort_popularity.setOnClickListener {
            if (it?.isSelected.falseIfNull()) return@setOnClickListener
            it?.isSelected = !it.isSelected
            btn_sort_date?.isSelected = false
            viewModel.tempSearchOptions?.sortBy = SortByType.POPULARITY
        }
    }

    fun resumeFilterState() {
        val searchOptions = viewModel.searchOptions.value
        val sources = viewModel.sources.value
        tv_date_from.text = searchOptions?.dateFrom
        tv_date_to.text = searchOptions?.dateTo
        sources_spinner.value = sources?.indexOfFirst { it.name == searchOptions?.source } ?: 0
        btn_sort_date.isSelected = searchOptions?.sortBy == SortByType.DATE
        btn_sort_popularity.isSelected = searchOptions?.sortBy == SortByType.POPULARITY
    }

    fun resetFilter() {
        tv_date_from.text = viewModel.getInitialDateFrom()
        tv_date_to.text = viewModel.getInitialDateTo()
        sources_spinner.value = 0
        btn_sort_date.isSelected = true
        btn_sort_popularity.isSelected = false
    }

    fun setLayoutWidthAndHeight(containerElement: View, width: Int, height: Int) {
        val layoutParams = containerElement.layoutParams
        layoutParams.width = width
        layoutParams.height = height
        containerElement.layoutParams = layoutParams
    }

    fun startExpandAnimation(containerElement: View) {
        val animator = ValueAnimator.ofFloat(0f, 100f)
        animator.addUpdateListener { valueAnimator ->
            val x = valueAnimator.animatedValue as Float
            val width = WIDTH.toPx() + WIDTH.toPx() * x / 100f
            val height = MIN_HEIGHT.toPx() + MAX_HEIGHT.toPx() * x / 100f
            setLayoutWidthAndHeight(containerElement, width.toInt(), height.toInt())
        }
        animator.start()
    }

    fun startCollapseAnimation(containerElement: View) {
        val animator = ValueAnimator.ofFloat(100f, 0f)
        animator.addUpdateListener { valueAnimator ->
            val x = valueAnimator.animatedValue as Float
            val width = WIDTH.toPx() + WIDTH.toPx() * x / 100f
            val height = MIN_HEIGHT.toPx() + MAX_HEIGHT.toPx() * x / 100f
            setLayoutWidthAndHeight(containerElement, width.toInt(), height.toInt())
        }
        animator.start()
    }


}