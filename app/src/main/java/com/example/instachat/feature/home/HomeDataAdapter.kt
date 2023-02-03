package com.example.instachat.feature.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.ItemHomeFragmentBinding
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeDataAdapter constructor(val viewModel: HomeViewModel) :
    ListAdapter<HomeDataModel1, HomeDataViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeDataViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemHomeFragmentBinding>(
            inflator,
            R.layout.item_home_fragment,
            parent,
            false
        )
        return HomeDataViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: HomeDataViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HomeDataViewHolder(val binding: ItemHomeFragmentBinding, val viewModel: HomeViewModel) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(homeData: HomeDataModel1) {
        binding.commentSection.viewModel = viewModel
        binding.viewModel = viewModel
        binding.homeDataModel1 = homeData
        if(homeData.comments.isNotEmpty()){
            binding.firstComment = homeData.comments.first()
        }

        if(homeData.homeDataModel.likedPosts.isNotEmpty()){
            if(homeData.homeDataModel.likedPosts.map { it.postId }.contains(homeData.homeDataModel.postId)){
                binding.bottomTools.imageView4.setImageResource(R.drawable.icon_like_clicked)
            }else{
                binding.bottomTools.imageView4.setImageResource(R.drawable.post_like)
            }
        }else{
            binding.bottomTools.imageView4.setImageResource(R.drawable.post_like)
        }


        binding.bottomTools.imageView4.setOnClickListener {

            if(homeData.homeDataModel.likedPosts.isNotEmpty()){
                if(homeData.homeDataModel.likedPosts.map { it.postId }.contains(homeData.homeDataModel.postId)){
                    binding.bottomTools.imageView4.setImageResource(R.drawable.post_like)
                }else{
                    binding.bottomTools.imageView4.setImageResource(R.drawable.icon_like_clicked)
                }
            }else{
                binding.bottomTools.imageView4.setImageResource(R.drawable.icon_like_clicked)
            }

            viewModel.onLikeButtonClicked(homeData.homeDataModel, adapterPosition)

        }



        binding.commentSection.etAddComment.setOnClickListener {
            viewModel.onCommentsTextClicked(homeData.homeDataModel.postId, adapterPosition)
        }
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<HomeDataModel1>() {
    override fun areItemsTheSame(oldItem: HomeDataModel1, newItem: HomeDataModel1): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HomeDataModel1, newItem: HomeDataModel1): Boolean {
        return oldItem.homeDataModel.postId == newItem.homeDataModel.postId
    }

}