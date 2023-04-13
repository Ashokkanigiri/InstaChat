package com.example.instachat.feature.userdetails

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.instachat.BaseActivity
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentUserDetailsBinding
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.RequestedForInterestModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

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
        observeViewModel()
    }

    private fun loadViewModel() {
    }

    private fun observeViewModel() {
        viewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserDetailViewModelEvent.LoadUser -> {
                    binding.user = it.user
                    (activity as BaseActivity).setBackLabelText(it.user.username)
                    viewModel.loadAllPostsForUser(viewModel.userId?:"")
                }
                is UserDetailViewModelEvent.LoadPosts -> {
                    viewModel.adapter.submitList(it.posts)
                }
                UserDetailViewModelEvent.OnFollowButtonClicked -> {

                }
                UserDetailViewModelEvent.OnMessageButtonClicked -> {

                }
                is UserDetailViewModelEvent.OnFollowStatusRequested ->{
                    listenToFollowStatusRequested(it.workId, it.interestedUsersModel, it.requestedForInterestModel)
                }
                is UserDetailViewModelEvent.LoadLoggedUser ->{
                    viewModel.loadFollowingText(it.user)
                }
            }
        })
    }

    private fun listenToFollowStatusRequested(
        workId: UUID,
        interestedUsersModel: InterestedUsersModel,
        requestedForInterestModel: RequestedForInterestModel
    ){
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workId).observe(viewLifecycleOwner, Observer {
            when(it.state){
                WorkInfo.State.SUCCEEDED -> {
                    viewModel.addInterestedUserToLoggedUser(interestedUsersModel)
                    viewModel.addRequestForInterestTOCurrentUser(requestedForInterestModel)
                }
                WorkInfo.State.FAILED -> {
                    Log.d("kwjwkjgb", "FAILED")

                }
               else ->{

               }
            }
        })
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
            viewModel.apply {
                userId = it
                loadUser(it)
                loadLoggedUser()
            }
        }
    }
}