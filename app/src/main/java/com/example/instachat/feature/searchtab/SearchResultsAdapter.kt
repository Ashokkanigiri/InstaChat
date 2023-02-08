package com.example.instachat.feature.searchtab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.ItemLayoutSearchResultsBinding
import com.example.instachat.services.models.dummyjson.User

class SearchResultsAdapter: androidx.recyclerview.widget.ListAdapter<User, SearchResultsViewHolder>(SearchResultsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemLayoutSearchResultsBinding>(inflator, R.layout.item_layout_search_results, parent, false)
        return SearchResultsViewHolder((binding))
    }

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SearchResultsViewHolder(val binding: ItemLayoutSearchResultsBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(user: User){
        binding.user = user
    }
}

class SearchResultsDiffUtil: DiffUtil.ItemCallback<User>(){
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

}