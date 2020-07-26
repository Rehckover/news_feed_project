package com.example.vjetgrouptestapp.base.arch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BaseListAdapter<S : BaseItemState, I : Any>(
    diffUtilItemCallback: DiffUtil.ItemCallback<I>,
    var listener: Listener<I>
) : PagedListAdapter<I, BaseViewHolder<S, I>>(diffUtilItemCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<S, I> {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding: ViewDataBinding =
            DataBindingUtil.inflate(inflater, layoutResId(viewType), parent, false)
        return BaseViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<S, I>, position: Int) {
        val item = getItem(position)!!
        holder.bindItem(item, itemState(item, listener, position))
    }

    override fun onViewRecycled(holder: BaseViewHolder<S, I>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    protected abstract fun itemState(
        item: I,
        listener: Listener<I>,
        position: Int
    ): S

    @LayoutRes
    protected abstract fun layoutResId(viewType: Int): Int

    interface Listener<I> {
        fun onItemClick(item: I){}
    }
}