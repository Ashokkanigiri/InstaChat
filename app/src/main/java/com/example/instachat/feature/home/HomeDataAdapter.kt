package com.example.instachat.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.ItemHomeFragmentBinding
import com.example.instachat.services.models.PostModelItem

class HomeDataAdapter: ListAdapter<PostModelItem, HomeDataViewHolder>(DiffUtilCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeDataViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemHomeFragmentBinding>(inflator, R.layout.item_home_fragment, parent, false)
        return HomeDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeDataViewHolder, position: Int) {
       holder.bind(getItem(position))
    }
}

class HomeDataViewHolder(val binding: ItemHomeFragmentBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(postModelItem: PostModelItem){
        binding.post = postModelItem
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<PostModelItem>(){
    override fun areItemsTheSame(oldItem: PostModelItem, newItem: PostModelItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PostModelItem, newItem: PostModelItem): Boolean {
        return oldItem.id == newItem.id
    }

}