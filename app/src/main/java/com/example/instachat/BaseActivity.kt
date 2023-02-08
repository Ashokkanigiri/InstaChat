package com.example.instachat

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.instachat.databinding.LayoutToolbarBinding

open class BaseActivity : AppCompatActivity(){

    lateinit var toolBarBinding: LayoutToolbarBinding

    fun setupActionBar(binding: LayoutToolbarBinding){
        this.toolBarBinding = binding
    }

    fun setTitle(text: String){
        toolBarBinding.tvLabel.text = text
    }

    fun setBackButtonVisibility(shouldBackButtonVisible: Boolean){
        if(shouldBackButtonVisible == true){
            toolBarBinding.ivBack.visibility = View.VISIBLE
            toolBarBinding.tvBackLabel.visibility = View.VISIBLE
            toolBarBinding.tvLabel.visibility = View.GONE
        }else{
            toolBarBinding.ivBack.visibility = View.GONE
            toolBarBinding.tvBackLabel.visibility = View.GONE
            toolBarBinding.tvLabel.visibility = View.VISIBLE
        }
    }

    fun setAddPostIconVisibility(shouldSearchIconVisible: Boolean){
        if(shouldSearchIconVisible == true){
            toolBarBinding.ivAddPost.visibility = View.VISIBLE
        }else{
            toolBarBinding.ivAddPost.visibility = View.GONE
        }
    }

    fun setMessageIconvisibility(shouldMessageIconVisible: Boolean){
        if(shouldMessageIconVisible == true){
            toolBarBinding.ivMessages.visibility = View.VISIBLE
        }else{
            toolBarBinding.ivMessages.visibility = View.GONE
        }
    }

    fun handleBackPressed(backButtonClickListener: View.OnClickListener){
        toolBarBinding.ivBack.setOnClickListener(backButtonClickListener)
    }

    fun handleNewPostPressed(backButtonClickListener: View.OnClickListener){
        toolBarBinding.ivAddPost.setOnClickListener(backButtonClickListener)
    }

    fun setBackLabelText(text: String){
        toolBarBinding.tvBackLabel.setText(text)
    }
}