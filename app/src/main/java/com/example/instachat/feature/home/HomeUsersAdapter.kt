package com.example.instachat.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.ItemHomeUserAdapterBinding

class HomeUsersAdapter: ListAdapter<HomeDataModel1, HomeUsersViewHolder>(HomeUserDiffUtil()) {
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
    fun bind(homeDataModel: HomeDataModel1){
        binding.homeDataModel = homeDataModel.homeDataModel
    }
}

class HomeUserDiffUtil: DiffUtil.ItemCallback<HomeDataModel1>(){
    override fun areItemsTheSame(oldItem: HomeDataModel1, newItem: HomeDataModel1): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HomeDataModel1, newItem: HomeDataModel1): Boolean {
        return oldItem.homeDataModel.userId == newItem.homeDataModel.userId
    }

}