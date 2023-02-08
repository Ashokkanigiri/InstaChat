package com.example.instachat.feature.hometab.view

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
import com.example.instachat.feature.hometab.HomeViewModelEvent
import com.example.instachat.feature.hometab.viewmodel.HomeViewModel
import com.example.instachat.utils.DialogUtils
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
        handleSwipeLayout()
        loadViewModel()
        observeViewModel()
    }

    private fun loadViewModel() {
        viewModel.loadHomeData()
        viewModel.loadUsersData()
        viewModel.setUpActionBar()
    }

    private fun initFragment() {
        binding.lifecycleOwner = viewLifecycleOwner
        if (!viewModel.adapter.hasObservers()) viewModel.adapter.setHasStableIds(true)
        binding.viewModel = viewModel
        binding.rvHome.adapter = viewModel.adapter

        arguments?.getInt("postId")?.let {
            viewModel.selectedPostId = it
            viewModel.isFromSearchFragment = true
            binding.rvUsers.visibility = View.GONE
        }
    }

    private fun observeViewModel() {

        viewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is HomeViewModelEvent.ShowConnectivityErrorDialog -> {
                    DialogUtils.populateConnectivityErrorDialog(requireActivity())
                }
                is HomeViewModelEvent.ShowActionBarForHome -> {
                    populateActionBarForHome()
                }
                is HomeViewModelEvent.ShowActionBarFromSearch -> {
                    populateActionBarFromSearch()
                }
                is HomeViewModelEvent.LoadHomeData -> {
                    viewModel.adapter.submitList(it.data)
                }
                is HomeViewModelEvent.LoadHomeUsersData -> {
                    viewModel.usersAdapter.submitList(it.data)
                }
                is HomeViewModelEvent.LoadHomeDataFromSearch -> {
                    viewModel.adapter.submitList(it.data)
                }
                is HomeViewModelEvent.NavigateFromHomeToCommentsFragment -> {
                    navigateToCommentsFragment(it.postId)
                }
            }
        })
    }

    private fun navigateToCommentsFragment(postId: Int?) {
        postId?.let {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeToCommentFragment(it)
            )
        }
    }

    private fun handleSwipeLayout() {
        binding.swipeLayout.setOnRefreshListener {
            viewModel.injectDataFromFirebase()
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun navigateToNewPostFragment() {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeToNewPostFragment()
        )
    }

    private fun populateActionBarForHome() {
        (activity as BaseActivity).setupActionBar(binding.actionBar)
        (activity as BaseActivity).setBackButtonVisibility(false)
        (activity as BaseActivity).setAddPostIconVisibility(true)
        (activity as BaseActivity).setMessageIconvisibility(true)
        (activity as BaseActivity).handleNewPostPressed {
            navigateToNewPostFragment()
        }
    }

    private fun populateActionBarFromSearch() {
        (activity as BaseActivity).setupActionBar(binding.actionBar)
        (activity as BaseActivity).setBackButtonVisibility(true)
        (activity as BaseActivity).setAddPostIconVisibility(false)
        (activity as BaseActivity).setMessageIconvisibility(false)
        (activity as BaseActivity).setBackLabelText("Explore")
        (activity as BaseActivity).handleBackPressed { findNavController().popBackStack() }

    }
}