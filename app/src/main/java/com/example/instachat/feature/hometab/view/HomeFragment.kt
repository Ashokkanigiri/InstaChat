package com.example.instachat.feature.hometab.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instachat.BaseActivity
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentHomeBinding
import com.example.instachat.feature.hometab.HomeViewModelEvent
import com.example.instachat.feature.hometab.viewmodel.HomeViewModel
import com.example.instachat.utils.ConstantUtils
import com.example.instachat.utils.DialogUtils
import com.example.instachat.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
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
        viewModel.loadData()
        viewModel.loadUsersData()
        viewModel.setUpActionBar()
    }

    private fun initFragment() {
        binding.lifecycleOwner = viewLifecycleOwner
        if (!viewModel.adapter.hasObservers()) viewModel.adapter.setHasStableIds(true)
        binding.viewModel = viewModel
        binding.rvHome.adapter = viewModel.adapter

        arguments?.getInt("postId")?.let {
            viewModel.isFromSearchFragment = true
            binding.rvUsers.visibility = View.GONE
        }

        listenForNewPostWorkId()
    }

    private fun listenForNewPostWorkId() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<UUID>(ConstantUtils.WorkIdKeys.NEW_POST_WORK_ID_KEY)?.observe(viewLifecycleOwner) {
            it?.let {
                Log.d("wjbgfwjkgbf", "WORKID : ${it}")
            }
        }
    }

    private fun observeViewModel() {

        viewModel.event.observe(viewLifecycleOwner, Observer {
            Log.d("wqfkqwnfq", "EVENT :: ${it.toString()}")

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
                is HomeViewModelEvent.NavigateFromHomeToCommentsFragment -> {
                    navigateToCommentsFragment(it.postId)
                }
                is HomeViewModelEvent.NavigateToUserDetailScreen -> {
                    navigateToUserDetails(it.userId)
                }
            }
        })
        viewModel.loadHomeDataEvent.observe(viewLifecycleOwner, Observer {
            when(it){
                is Response.Failure -> {

                }
                Response.Loading -> {

                }
                is Response.Success -> {
                    viewModel.adapter.submitList(it.data)
                }
            }
        })

        viewModel.loadSearchDataEvent.observe(viewLifecycleOwner, Observer {
            when(it){
                is Response.Failure -> {

                }
                Response.Loading -> {

                }
                is Response.Success -> {
                    viewModel.adapter.submitList(it.data)
                }
            }
        })
        viewModel.loadHomeUsersDataEvent.observe(viewLifecycleOwner, Observer {
            when(it){
                is Response.Failure -> {

                }
                Response.Loading -> {

                }
                is Response.Success -> {
                    viewModel.usersAdapter.submitList(it.data)
                }
            }
        })
    }

    private fun navigateToUserDetails(userId: String?) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeToUserDetailsFragment(userId)
        )
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
        (activity as MainActivity).setBottomNavVisibility(true)
    }

    private fun populateActionBarFromSearch() {
        (activity as BaseActivity).setupActionBar(binding.actionBar)
        (activity as BaseActivity).setBackButtonVisibility(true)
        (activity as BaseActivity).setAddPostIconVisibility(false)
        (activity as BaseActivity).setMessageIconvisibility(false)
        (activity as BaseActivity).setBackLabelText("Explore")
        (activity as MainActivity).setBottomNavVisibility(false)
        (activity as BaseActivity).handleBackPressed { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}