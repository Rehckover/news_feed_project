package com.example.vjetgrouptestapp.base.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vjetgrouptestapp.BR


abstract class BaseFragment<V : BaseViewModel>
    : androidx.fragment.app.Fragment() {

    protected lateinit var binding: ViewDataBinding
    protected lateinit var viewModel: V


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeToEvents()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(viewModelClass())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    override fun onStop() {
        super.onStop()
        showLoading(false)
    }

    protected abstract fun viewModelClass():Class<V>

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected open fun subscribeToEvents() {
        subscribeProgressEvents()
    }

    open fun onBackPressed(): Boolean {
        return true
    }

    protected open fun showLoading(visible: Boolean) {
        (activity as BaseActivity<*, *>).showLoading(visible)
    }

    private fun subscribeProgressEvents() {
        viewModel.progressVisibility.observe(this, Observer {
            showLoading(it)
        })

    }
}