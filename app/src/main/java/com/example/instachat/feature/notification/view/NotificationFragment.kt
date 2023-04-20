package com.example.instachat.feature.notification.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.instachat.BaseActivity
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentNewPostDetailBinding
import com.example.instachat.databinding.FragmentNotificationBinding
import com.example.instachat.feature.notification.viewmodel.NotificationViewModel
import com.example.instachat.services.models.rest.NotificationModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    val viewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.loadData()
        setUpActionBar()
        handleSwipeLayout()
    }

    private fun setUpActionBar() {
        (activity as BaseActivity).setupActionBar(binding.actionBar)
        (activity as BaseActivity).setBackButtonVisibility(false)
        (activity as BaseActivity).setAddPostIconVisibility(false)
        (activity as BaseActivity).setMessageIconvisibility(false)
        (activity as MainActivity).setBottomNavVisibility(true)
        (activity as BaseActivity).setTitle("Notifications")
    }

    private fun handleSwipeLayout() {
        binding.swipeLayout.setOnRefreshListener {
            viewModel.injectData()
            binding.swipeLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvNotifications.adapter = null
        viewModel.clearAdapters()
        _binding = null
    }
}