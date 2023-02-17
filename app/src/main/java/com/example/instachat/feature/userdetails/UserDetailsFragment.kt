package com.example.instachat.feature.userdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instachat.BaseActivity
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    lateinit var binding: FragmentUserDetailsBinding

    val viewModel: UserDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        initFragment()
        loadViewModel()
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

    private fun initFragment() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        if (!viewModel.adapter.hasObservers()) viewModel.adapter.setHasStableIds(true)
        val layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.rvPosts.layoutManager = layoutManager
        binding.rvPosts.adapter = viewModel.adapter

        arguments?.getString("userId")?.let {
            viewModel.loadUser(it)
        }
    }

    private fun loadViewModel() {
        viewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserDetailViewModelEvent.LoadUser -> {
                    binding.user = it.user
                    (activity as BaseActivity).setBackLabelText(it.user.username)
                    viewModel.loadAllPostsForUser(it.user.id)
                }
                is UserDetailViewModelEvent.LoadPosts -> {
                    viewModel.adapter.submitList(it.posts)
                }
            }
        })
    }
}