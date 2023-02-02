package com.example.instachat.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.FragmentCommentBinding
import com.example.instachat.databinding.LayoutItemCommentBinding
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class CommentAdapter: ListAdapter<Comment, CommentViewHolder>(CommentDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LayoutItemCommentBinding>(inflator, R.layout.layout_item_comment, parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CommentViewHolder(val binding: LayoutItemCommentBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(comment: Comment){
        binding.comment = comment
        getUser(comment.user.id)
    }

    fun getUser(userId: String){
        val db = Firebase.firestore
        db.collection("users").whereEqualTo("id", userId).addSnapshotListener { value, error ->
            val user = value?.documents?.map { it.data }?.map {it ->
                Gson().fromJson(Gson().toJson(it), User::class.java)
            }
            if(user?.isNotEmpty()?:false){
                binding.user = user?.get(0)
            }else{
                getUser("10")
            }
        }
    }
}

class CommentDiffUtil: DiffUtil.ItemCallback<Comment>(){
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

}