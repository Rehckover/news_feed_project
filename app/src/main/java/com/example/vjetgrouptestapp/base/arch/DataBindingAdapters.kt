package com.example.vjetgrouptestapp.base.arch

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.vjetgrouptestapp.base.extensions.toPx

@BindingAdapter(
    value = [
        "app:loadImage",
        "app:cornerRadius"
    ], requireAll = false
)
fun loadImage(imageView: ImageView, url: String?, cornerRadius: Int?) {
    if (url == null) return
    val multiTransformation = MultiTransformation(
        CenterCrop(),
        RoundedCorners(cornerRadius?.toPx() ?: 0)
    )

    Glide.with(imageView.context)
        .load(url)
        .apply(bitmapTransform(multiTransformation))
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}