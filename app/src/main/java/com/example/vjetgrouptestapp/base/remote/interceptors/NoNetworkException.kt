package com.example.vjetgrouptestapp.base.remote.interceptors

import com.example.vjetgrouptestapp.App
import com.example.vjetgrouptestapp.R
import java.io.IOException

class NoNetworkException : IOException(App.applicationContext().getString(R.string.error_no_network))