package com.example.vjetgrouptestapp.base.utils

import android.app.DatePickerDialog
import android.content.Context
import java.util.*


object DialogHelper {
    fun setDate(context: Context, listener: DatePickerDialog.OnDateSetListener) {
        val dateAndTime: Calendar = Calendar.getInstance()
        DatePickerDialog(
            context, listener,
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}