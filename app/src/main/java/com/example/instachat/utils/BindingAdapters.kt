package com.example.instachat.utils

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.instachat.R
import com.example.instachat.feature.hometab.models.HomeDataModel
import com.example.instachat.services.GlideApp
import com.example.instachat.services.models.dummyjson.Comment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

@BindingAdapter("loadImageWithGlide")
fun ImageView.loadImageWithGlide(imageUrls: List<String>?){
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

   imageUrls?.let {it ->
       it.firstOrNull()?.let {
           GlideApp.with(this).load(it)
               .diskCacheStrategy(DiskCacheStrategy.DATA)
               .placeholder(circularProgressDrawable)
               .into(this)
       }
   }
}

@BindingAdapter("loadImageUriWithGlide")
fun ImageView.loadImageUriWithGlide(uri: Uri){
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    GlideApp.with(this).load(uri)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .placeholder(circularProgressDrawable)
        .into(this)
}

@BindingAdapter("loadCircularImageWithGlide")
fun ImageView.loadCircularImageWithGlide( imageUrl: String?){

    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()


    if(imageUrl.isNullOrEmpty()){
        GlideApp.with(this).load("https://robohash.org/delenitipraesentiumvoluptatum.png")
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(circularProgressDrawable)
            .into(this)
    }else{
        GlideApp.with(this).load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(circularProgressDrawable)
            .into(this)
    }


}

@BindingAdapter("loadCircularImageWithGlideWithDefault")
fun ImageView.loadCircularImageWithGlideWithDefault( imageUrl: String?){

    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    if(imageUrl == null){
        GlideApp.with(this).load(R.drawable.user_add)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(circularProgressDrawable)
            .into(this)
    }

    GlideApp.with(this).load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .apply(RequestOptions.circleCropTransform())
        .placeholder(R.drawable.user_add)
        .placeholder(circularProgressDrawable)
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
    if(homeData.likedPosts?.isNotEmpty() ?: false){
        if(homeData.likedPosts?.map { it.postId }?.contains(homeData.postId) ?: false){
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
    if(!tags.isNullOrEmpty()) {
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

@BindingAdapter("shouldClearAndHideFocus")
fun EditText.shouldClearAndHideFocus(shouldClearAndHideFocus: Boolean){
    if(shouldClearAndHideFocus){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
        this.clearFocus()
        this.text.clear()

    }
}

@BindingAdapter("displayErrorWhen", "errorText", requireAll = true)
fun TextInputLayout.displayErrorWhen(displayErrorWhen: Boolean, errorText: String){
    if(displayErrorWhen){
        this.error = errorText
    }else{
        this.error = null
    }
}