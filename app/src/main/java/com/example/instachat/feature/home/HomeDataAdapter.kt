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
    ListAdapter<PostModelItem, HomeDataViewHolder>(DiffUtilCallBack()) {

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

    fun bind(postModelItem: PostModelItem) {
        binding.post = postModelItem
        getUser(postModelItem.userId)
        getFirstCommentForPost(postModelItem.id)

        binding.commentSection.etAddComment.setOnClickListener {
            viewModel.onCommentsTextClicked(postModelItem.id, adapterPosition)
        }
    }

    fun getUser(userId: Int?) {
       viewModel.getPostedUser(userId) {
           it?.let {
               binding.user = it
           }
       }
    }

    fun getFirstCommentForPost(postId: Int) {
        viewModel.getFirstCommentForPost(postId){
            binding.comment = it
        }
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<PostModelItem>() {
    override fun areItemsTheSame(oldItem: PostModelItem, newItem: PostModelItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PostModelItem, newItem: PostModelItem): Boolean {
        return oldItem.id == newItem.id
    }

}