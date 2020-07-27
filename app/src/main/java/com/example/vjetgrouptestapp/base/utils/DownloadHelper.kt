package com.example.vjetgrouptestapp.base.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.vjetgrouptestapp.base.db.DbSettings
import com.example.vjetgrouptestapp.base.extensions.getDownloadManager
import com.example.vjetgrouptestapp.base.extensions.isEmptyOrNull
import java.io.File


object DownloadHelper {

    fun downloadImageByUrl(context: Context, url: String?) {
        if (url.isEmptyOrNull()) return
        val fileName = url!!.substring(url.lastIndexOf('/') + 1)
        val downloadUri: Uri = Uri.parse(url) ?: return
        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(fileName)
            .setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                File.separator + DbSettings.DATABASE_NAME + File.separator.toString() + fileName
            )
        context.getDownloadManager().enqueue(request)
    }
}
