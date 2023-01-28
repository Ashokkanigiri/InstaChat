package com.example.instachat.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.ItemHomeUserAdapterBinding
import com.example.instachat.services.models.dummyjson.User

class HomeUsersAdapter: ListAdapter<User, HomeUsersViewHolder>(HomeUserDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeUsersViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemHomeUserAdapterBinding>(inflator, R.layout.item_home_user_adapter, parent, false)
        return HomeUsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeUsersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HomeUsersViewHolder(val binding: ItemHomeUserAdapterBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(user: User){
        binding.user = user
    }
}

class HomeUserDiffUtil: DiffUtil.ItemCallback<User>(){
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

}