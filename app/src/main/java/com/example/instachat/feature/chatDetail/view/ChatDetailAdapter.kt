package com.example.instachat.feature.chatDetail.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.LayoutChatReceiveBinding
import com.example.instachat.databinding.LayoutChatSendBinding
import com.example.instachat.feature.chatDetail.viewmodel.ChatDetailViewModel
import com.example.instachat.services.models.rest.ChatModel

class ChatDetailAdapter constructor(val viewmodel: ChatDetailViewModel, val loggedUserId: String) :
    ListAdapter<ChatModel, BaseViewHolder>(ChatDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val sendBinding = DataBindingUtil.inflate<LayoutChatSendBinding>(
            inflator,
            R.layout.layout_chat_send,
            parent,
            false
        )
        val receiveBinding = DataBindingUtil.inflate<LayoutChatReceiveBinding>(
            inflator,
            R.layout.layout_chat_receive,
            parent,
            false
        )

        if(viewType == 1001){
            return ChatSendViewHolder(sendBinding)
        }

        return ChatReceiveViewHolder(receiveBinding)

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).sentUserId == loggedUserId){
            1001
        }else{
            1111
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }
}

class ChatSendViewHolder(val binding: LayoutChatSendBinding) : BaseViewHolder(binding.root) {
    override fun bind(chatModel: ChatModel) {
        binding.chatModel = chatModel
    }
}

class ChatReceiveViewHolder(val binding: LayoutChatReceiveBinding) : BaseViewHolder(binding.root) {
    override fun bind(chatModel: ChatModel) {
        binding.chatModel = chatModel
    }
}

open class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view.rootView){
    open fun bind(chatModel: ChatModel){

    }
}


class ChatDiffUtil : DiffUtil.ItemCallback<ChatModel>() {
    override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
        return oldItem.id == newItem.id
    }
}