package com.example.vjetgrouptestapp.base.arch

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.example.vjetgrouptestapp.BR


open class BaseViewHolder<in S : BaseItemState, I : Any>(
    val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    override fun getLifecycle() = lifecycleRegistry

    open fun bindItem(item: I, itemState: S) {
        binding.lifecycleOwner = this
        binding.setVariable(BR.state, itemState)
        binding.executePendingBindings()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    open fun onViewRecycled() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

}