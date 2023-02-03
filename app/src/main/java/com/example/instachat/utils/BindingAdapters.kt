package com.example.instachat.utils

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.instachat.services.models.dummyjson.Comment

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

@BindingAdapter("getfirstCommentBody")
fun TextView.getfirstCommentBody(commentsList: List<Comment>?){
    commentsList?.let {list->
        if(list.isNotEmpty()){
            this.text = list.first().body
        }
    }
}

@BindingAdapter("viewAllCommentsVisibility")
fun TextView.viewAllCommentsVisibility(commentsList: List<Comment>?){
    commentsList?.let {list->
        if(list.isNotEmpty()){
            if(commentsList.size >1){
                this.visibility = View.VISIBLE
            }else{
                this.visibility = View.GONE
            }
        }else{
            this.visibility = View.GONE
        }
    }
}