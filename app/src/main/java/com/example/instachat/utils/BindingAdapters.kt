package com.example.instachat.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.instachat.BuildConfig

@BindingAdapter("loadImageWithGlide")
fun ImageView.loadImageWithGlide( imageUrl: String){
    Glide.with(this).load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}

@BindingAdapter("loadCircularImageWithGlide")
fun ImageView.loadCircularImageWithGlide( imageUrl: String){
    val glideUrl = GlideUrl("https://api.api-ninjas.com/v1/randomimage?category=people", LazyHeaders.Builder()
        .addHeader("X-Api-Key", BuildConfig.RANDOM_IMAGE_API_KEY).build() )

    Glide.with(this).load(glideUrl)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .apply(RequestOptions.circleCropTransform())
        .into(this)

}