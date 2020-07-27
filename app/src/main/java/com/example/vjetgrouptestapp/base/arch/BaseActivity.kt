package com.example.vjetgrouptestapp.base.arch

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vjetgrouptestapp.BR
import com.example.vjetgrouptestapp.R
import com.example.vjetgrouptestapp.base.extensions.toast

abstract class BaseActivity<V : BaseViewModel, B : ViewDataBinding>
    : AppCompatActivity() {

    protected lateinit var binding: B
    protected lateinit var viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =  ViewModelProvider(this).get(viewModelClass())
        binding = DataBindingUtil.setContentView(this, layoutResId())
        binding.setVariable(BR.viewModel, viewModel)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        subscribeToEvents()
    }

    protected abstract fun viewModelClass(): Class<V>

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected open fun subscribeToEvents() {
        viewModel.progressVisibility.observe(this, Observer {

        })
    }

    open fun showLoading(boolean: Boolean) {

    }

    open fun showNoInternetConnection() {
        toast(getString(R.string.error_no_network))
    }

    fun <T, LD : LiveData<T>> observeNullable(liveData: LD, onChanged: (T?) -> Unit) {
        liveData.observe(this, Observer { value ->
            onChanged(value)
        })
    }

    fun <T, LD : LiveData<T>> observe(liveData: LD, onChanged: (T) -> Unit) {
        liveData.observe(this, Observer { value ->
            value?.let(onChanged)
        })
    }
}