package com.example.vjetgrouptestapp.base.extensions

import android.content.res.Resources
import android.util.TypedValue
import com.example.vjetgrouptestapp.App


fun Int.toPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()
fun Int.spToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()
fun Int.getRes(vararg list: Any) = App.getStringFromRes(this, *list)
fun Int.getRes() = App.getStringFromRes(this)
fun Float.toPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics).toInt()

fun Boolean?.falseIfNull() = this ?: false

