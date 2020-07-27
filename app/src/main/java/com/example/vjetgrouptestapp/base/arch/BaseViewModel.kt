package com.example.vjetgrouptestapp.base.arch

import androidx.lifecycle.ViewModel
import com.example.vjetgrouptestapp.App
import com.example.vjetgrouptestapp.R
import com.example.vjetgrouptestapp.base.extensions.getRes
import com.example.vjetgrouptestapp.base.extensions.toast
import com.example.vjetgrouptestapp.base.remote.models.ErrorEntity
import com.example.vjetgrouptestapp.base.remote.models.Result
import com.example.vjetgrouptestapp.base.utils.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {

    val progressVisibility = SingleLiveEvent<Boolean>()

    protected fun showLoading() {
        progressVisibility.postValue(true)
    }

    protected fun hideLoading() {
        progressVisibility.postValue(false)
    }

    protected fun showError(
        message: String?,
        cancelListener: (() -> Unit)? = null,
        retryListener: (() -> Unit)? = null
    ) {
        // if no dialog show toast
        message?.let {
            App.applicationContext().toast(it)
        }
    }

    fun handleErrors(
        result: Result.Error,
        retryListener: (() -> Unit)? = null
    ) {
        when (result.error) {
            is ErrorEntity.NoNetwork -> {
                showError(R.string.error_no_network.getRes())
            }
            is ErrorEntity.Unauthorized -> {

            }
            is ErrorEntity.BadRequest -> {

            }
            else -> {
                showError("something went wrong")
            }
        }
    }
}
