package com.example.instachat.feature.notification.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.LayoutItemNotificationsBinding
import com.example.instachat.services.models.rest.NotificationModel

class NotificationsAdapter : ListAdapter<NotificationModel, NotificationsViewHolder>(NotificationsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val binding = DataBindingUtil.inflate<LayoutItemNotificationsBinding>(LayoutInflater.from(parent.context), R.layout.layout_item_notifications, parent, false)
        return NotificationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class NotificationsViewHolder (val binding: LayoutItemNotificationsBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(notificationModel: NotificationModel){
        binding.notification = notificationModel
    }
}

class NotificationsDiffUtil : DiffUtil.ItemCallback<NotificationModel>(){
    override fun areItemsTheSame(oldItem: NotificationModel, newItem: NotificationModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: NotificationModel,
        newItem: NotificationModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

}