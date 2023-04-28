package com.example.instachat.feature.chatDetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.instachat.BaseActivity
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentUserDetailsBinding
import com.example.instachat.databinding.LayoutFragmentChatDetailBinding
import com.example.instachat.feature.chatDetail.viewmodel.ChatDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatDetailFragment: Fragment() {

    private var _binding: LayoutFragmentChatDetailBinding? = null
    private val binding get() = _binding!!

    val viewModel : ChatDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_chat_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragment()
        loadArguments()
        setUpToolBar()
        loadData()
    }

    private fun initFragment() {
        binding.lifecycleOwner = viewLifecycleOwner

        if (!viewModel.chatAdapter.hasObservers()) viewModel.chatAdapter.setHasStableIds(true)
            binding.viewModel = viewModel
        binding.rvChatDetail.adapter = viewModel.chatAdapter

    }

    private fun loadArguments() {
        arguments?.getString("userId")?.let {
            viewModel.userId = it
        }
    }

    private fun loadData() {
        viewModel.getData()
    }

    private fun setUpToolBar() {
        (activity as BaseActivity).setupActionBar(binding.toolBar)
        (activity as MainActivity).setBottomNavVisibility(false)
        (activity as BaseActivity).setBackButtonVisibility(true)
        (activity as BaseActivity).setAddPostIconVisibility(false)
        (activity as BaseActivity).setMessageIconvisibility(false)
        (activity as MainActivity).setBottomNavVisibility(false)
        (activity as BaseActivity).handleBackPressed { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}