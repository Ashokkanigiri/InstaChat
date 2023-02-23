package com.example.instachat.feature.newpostDetail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.ItemNewPostDetailBinding

class NewPostDetailAdapter: ListAdapter<Uri, NewPostDetailViewHolder >(NewPostDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewPostDetailViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemNewPostDetailBinding>(inflator, R.layout.item_new_post_detail, parent, false)
        return NewPostDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewPostDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class NewPostDetailViewHolder(val binding: ItemNewPostDetailBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(uri: Uri){
        binding.imageUri = uri
    }
}

class NewPostDiffUtil: DiffUtil.ItemCallback<Uri>(){
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }

}