package com.example.instachat.feature.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.ItemHomeFragmentBinding
import com.google.gson.Gson

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
        binding.commentSection.viewModel = viewModel
        binding.viewModel = viewModel
        binding.homeDataModel = homeData

        viewModel.getFirstCommentForPost(homeData.postId){
            binding.commentSection.homeDataCommentsModel = it
        }

        if(homeData.likedPosts.isNotEmpty()){
            if(homeData.likedPosts.map { it.postId }.contains(homeData.postId)){
                Log.d("qjfbqf", "\n ${homeData.postId} contains post id -> like red")
                binding.bottomTools.imageView4.setImageResource(R.drawable.icon_like_clicked)
            }else{
                Log.d("qjfbqf", "\n" +
                        " ${homeData.postId} not contains post id -> like blank")
                binding.bottomTools.imageView4.setImageResource(R.drawable.post_like)
            }
        }else{
            Log.d("qjfbqf", " \n" +
                    " ${homeData.postId}list empty :: ${Gson().toJson(homeData)} -> like blank")
            binding.bottomTools.imageView4.setImageResource(R.drawable.post_like)
        }


        binding.bottomTools.imageView4.setOnClickListener {
            viewModel.onLikeButtonClicked(homeData)
        }

        binding.commentSection.etAddComment.setOnClickListener {
            viewModel.onCommentsTextClicked(homeData.postId, adapterPosition)
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