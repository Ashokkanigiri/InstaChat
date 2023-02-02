package com.example.instachat.utils

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget

@BindingAdapter("loadImageWithGlide")
fun ImageView.loadImageWithGlide( imageUrl: String){
    Glide.with(this).load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}

@BindingAdapter("loadCircularImageWithGlide")
fun ImageView.loadCircularImageWithGlide( imageUrl: String?){

    Glide.with(this).load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

@BindingAdapter("displayHashTags")
fun TextView.displayHashTags(hashTags: List<String>?){
    hashTags?.forEach {
        this.text =  "${this.text}, #${it}"
    }
}