package com.example.instachat.feature.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instachat.BaseActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentHomeBinding
import com.example.instachat.feature.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
        setUpActionBar()
        handleSwipeLayout()
        observeViewModel()
    }

    private fun initFragment() {
        binding.lifecycleOwner = viewLifecycleOwner
        if (!viewModel.adapter.hasObservers()) viewModel.adapter.setHasStableIds(true)
        binding.viewModel = viewModel
        binding.rvHome.adapter = viewModel.adapter
    }

    private fun observeViewModel() {
        viewModel.roomRepository.usersDao.getallUsers().observe(viewLifecycleOwner, Observer {
            viewModel.usersAdapter.submitList(it)
        })

        viewModel.roomRepository.postsDao.getPostsHomeDataLive(viewModel.auth.uid ?: "")
            .observe(viewLifecycleOwner, Observer {
                viewModel.adapter.submitList(it)
            })

        viewModel.commentsLayoutClickedEvent.observe(viewLifecycleOwner, Observer { postId ->
            postId?.let {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToCommentFragment(it)
                )
            }
        })
    }

    private fun handleSwipeLayout() {
        binding.swipeLayout.setOnRefreshListener {
            viewModel.injectDataFromFirebase()
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun setUpActionBar() {
        (activity as BaseActivity).setupActionBar(binding.actionBar)
        (activity as BaseActivity).setBackButtonVisibility(false)
        (activity as BaseActivity).setSearchIconvisibility(true)
        (activity as BaseActivity).setMessageIconvisibility(true)
    }
}