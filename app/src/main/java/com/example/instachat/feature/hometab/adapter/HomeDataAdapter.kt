package com.example.instachat.feature.hometab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.ItemHomeFragmentBinding
import com.example.instachat.feature.hometab.models.HomeDataModel
import com.example.instachat.feature.hometab.viewmodel.HomeViewModel

class HomeDataAdapter constructor(val viewModel: HomeViewModel) :
    ListAdapter<HomeDataModel, HomeDataViewHolder>(DiffUtilCallBack()) {

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

    override fun getItemId(position: Int): Long {
        return getItem(position).postId.toLong()
    }
}


class HomeDataViewHolder(val binding: ItemHomeFragmentBinding, val viewModel: HomeViewModel) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(homeData: HomeDataModel) {
        binding.viewModel = viewModel
        binding.homeDataModel = homeData

        viewModel.getFirstCommentForPost(homeData.postId){

            binding.commentSection.homeDataCommentsModel = it
        }
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<HomeDataModel>() {
    override fun areItemsTheSame(oldItem: HomeDataModel, newItem: HomeDataModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HomeDataModel, newItem: HomeDataModel): Boolean {
        return oldItem.postId == newItem.postId
    }
}