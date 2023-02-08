package com.example.instachat.feature.searchtab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.ItemRvSearchtabBinding
import com.example.instachat.services.models.PostModelItem

class SearchTabHomeAdapter constructor(val searchTabViewModel: SearchTabViewModel) :
    ListAdapter<PostModelItem, SearchTabHomeViewHolder>(SearchTabDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTabHomeViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemRvSearchtabBinding>(
            inflator,
            R.layout.item_rv_searchtab,
            parent,
            false
        )
        return SearchTabHomeViewHolder(binding, searchTabViewModel)
    }

    override fun onBindViewHolder(holder: SearchTabHomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SearchTabHomeViewHolder(
    val binding: ItemRvSearchtabBinding,
    val searchTabViewModel: SearchTabViewModel
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(postModelItem: PostModelItem) {
        binding.post = postModelItem
        binding.viewModel = searchTabViewModel
    }
}

class SearchTabDiffUtil : DiffUtil.ItemCallback<PostModelItem>() {
    override fun areItemsTheSame(oldItem: PostModelItem, newItem: PostModelItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PostModelItem, newItem: PostModelItem): Boolean {
        return oldItem.id == newItem.id
    }

}

