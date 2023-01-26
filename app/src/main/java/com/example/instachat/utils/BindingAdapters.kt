package com.example.instachat.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("loadImageWithGlide")
fun ImageView.loadImageWithGlide( imageUrl: String){
    Glide.with(this).load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}