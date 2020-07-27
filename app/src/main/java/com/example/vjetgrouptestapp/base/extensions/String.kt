package com.example.vjetgrouptestapp.base.extensions

import android.util.Log
import com.example.vjetgrouptestapp.base.utils.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

fun String.reformatDate(newFormat: String = DATE_FORMAT):String {
    return try {
        /** DEBUG dateStr = '2006-04-16T04:00:00Z' **/
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val mDate = formatter.parse(this) // this never ends while debugging
        val format = SimpleDateFormat(newFormat,Locale.ENGLISH)
        format.format(mDate!!)
    } catch (e: Exception) {
        Log.e("mDate", e.toString()) // this never gets called either
        ""
    }
}

fun String?.isEmptyOrNull():Boolean{
    return this.isNullOrEmpty() ||
            this == "null"
}