package com.example.androidmvvm.core.extension

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide

fun AppCompatImageView.setImageUrl(url: String?, @DrawableRes placeholder: Int? = null) {
    // Skip loading if URL is null or empty to avoid unnecessary Glide calls
    if (url.isNullOrEmpty()) {
        placeholder?.let {
            setImageDrawable(AppCompatResources.getDrawable(context, it))
        }
        return
    }
    
    Glide.with(context)
        .load(url)
        .apply {
            placeholder?.let {
                placeholder(AppCompatResources.getDrawable(context, it))
            }
        }
        .into(this)
}