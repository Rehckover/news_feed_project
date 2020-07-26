package com.example.vjetgrouptestapp

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        public var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun getStringFromRes(@StringRes stringId: Int): String {
            return instance!!.getString(stringId)
        }

        fun getStringFromRes(@StringRes stringId: Int, vararg formatArgs: Any): String {
            return instance!!.getString(stringId, *formatArgs)
        }
    }
}

