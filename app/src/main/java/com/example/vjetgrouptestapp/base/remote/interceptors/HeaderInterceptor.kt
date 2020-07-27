package com.example.vjetgrouptestapp.base.remote.interceptors

import com.example.vjetgrouptestapp.App
import com.example.vjetgrouptestapp.base.extensions.hasNetworkConnection
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {

        //check internet
        if (!App.applicationContext().hasNetworkConnection())
            throw NoNetworkException()
        val newRequest = chain?.request()?.newBuilder()?.apply {
            header(
                HEADER_ACCEPT,
                ACCEPT_TYPE
            )
        }?.build()

        return chain?.proceed(newRequest!!)!!
    }

    companion object {

        private const val TAG = "RETROFIT"
        private const val TAG_HEADER = "HEADER_INTERCEPTOR"

        private const val HEADER_REFRESHED_TOKEN = "Refreshed-Token"
        private const val HEADER_AUTHORIZATION = "x-api-token"
        private const val HEADER_ACCEPT = "Accept"
        private const val ACCEPT_TYPE = "application/json"
    }
}