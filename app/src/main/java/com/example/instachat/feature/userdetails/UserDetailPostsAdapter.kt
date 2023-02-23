package com.example.instachat.feature.userdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.ItemPostsUserDetailsBinding

class UserDetailPostsAdapter :
    ListAdapter<UserDetailPostsModel, UserDetailPostsViewHolder>(UserDetailPostsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailPostsViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemPostsUserDetailsBinding>(
            inflator,
            R.layout.item_posts_user_details,
            parent,
            false
        )
        return UserDetailPostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserDetailPostsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).postId.toLong()
    }
}

class UserDetailPostsViewHolder(val binding: ItemPostsUserDetailsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: UserDetailPostsModel) {
        binding.userDetailPostsModel = model
    }
}

class UserDetailPostsDiffUtil : DiffUtil.ItemCallback<UserDetailPostsModel>() {
    override fun areItemsTheSame(
        oldItem: UserDetailPostsModel,
        newItem: UserDetailPostsModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: UserDetailPostsModel,
        newItem: UserDetailPostsModel
    ): Boolean {
        return oldItem.postId == newItem.postId
    }

}