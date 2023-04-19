package com.example.instachat.feature.notification.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.LayoutItemNotificationsBinding
import com.example.instachat.databinding.LayoutNotificationHeaderBinding
import com.example.instachat.services.models.rest.NotificationModel

class NotificationHeaderAdapter  : ListAdapter<String, NotificationsHeaderViewHolder>(NotificationsHeaderDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsHeaderViewHolder {
        val binding = DataBindingUtil.inflate<LayoutNotificationHeaderBinding>(LayoutInflater.from(parent.context), R.layout.layout_notification_header, parent, false)
        return NotificationsHeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationsHeaderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class NotificationsHeaderViewHolder (val binding: LayoutNotificationHeaderBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(headerTitle: String){
        binding.title = headerTitle
    }
}

class NotificationsHeaderDiffUtil : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

}