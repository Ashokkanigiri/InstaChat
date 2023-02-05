package com.example.instachat.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.instachat.R
import com.example.instachat.feature.hometab.models.HomeDataModel
import com.example.instachat.services.models.dummyjson.Comment

@BindingAdapter("loadImageWithGlide")
fun ImageView.loadImageWithGlide(imageUrl: String){
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

@BindingAdapter("setLikeImageDrawable")
fun ImageView.setLikeImageDrawable(homeData: HomeDataModel){
    if(homeData.likedPosts.isNotEmpty()){
        if(homeData.likedPosts.map { it.postId }.contains(homeData.postId)){
            this.setImageResource(R.drawable.icon_like_clicked)
        }else{
            this.setImageResource(R.drawable.post_like)
        }
    }else{
        this.setImageResource(R.drawable.post_like)
    }
}

@BindingAdapter("loadNoInternetConnectionGif")
fun ImageView.loadNoInternetConnectionGif(boolean: Boolean){
    Glide.with(this).load(R.drawable.no_internet_connection).into(this)
}

@BindingAdapter("displayTag1ForPost")
fun TextView.displayTag1ForPost(tags: List<String>?){
    tags?.let {
        this.text = "#"+tags.get(0)
    }
}

@BindingAdapter("displayTag2ForPost")
fun TextView.displayTag2ForPost(tags: List<String>?){
    tags?.let {
        if(tags.size >= 2){
            this.text = "#"+tags.get(1)
        }else{
            this.visibility = View.GONE
        }
    }
}