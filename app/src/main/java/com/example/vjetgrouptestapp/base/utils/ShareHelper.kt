package com.example.vjetgrouptestapp.base.utils

import android.content.Context
import android.content.Intent
import com.example.vjetgrouptestapp.R
import com.example.vjetgrouptestapp.base.extensions.getRes


object ShareHelper {
    fun shareFeed(context: Context, title: String?, url: String?) {
        if (title == null || url == null) return
        val message = "$title\n$url"
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, message)

        context.startActivity(
            Intent.createChooser(
                share,
                R.string.share_news_title.getRes()
            )
        )
    }
}