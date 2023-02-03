package com.example.instachat.feature.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.instachat.BaseActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentHomeBinding
import com.example.instachat.services.repository.RoomRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var roomRepository: RoomRepository
    lateinit var binding: FragmentHomeBinding
    lateinit var  viewModel: HomeViewModel
    val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        setUpActionBar()
        loadViewModel()
        handleSwipeLayout()
        observeViewModel()
        handleRefreshPost()
    }

    private fun loadViewModel() {
        viewModel.loadViewModel()
    }

    private fun initFragment() {


    }

    private fun handleRefreshPost() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("Should_refresh_post")?.observe(viewLifecycleOwner) { shouldRefreshPost ->
            if(shouldRefreshPost){
                viewModel.refreshPost()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.commentsLayoutClickedEvent.observe(viewLifecycleOwner, Observer {postId->
            postId?.let {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToCommentFragment(it)
                )
            }
        })
    }

    private fun handleSwipeLayout() {
        binding.swipeLayout.setOnRefreshListener {
            loadDataFromViewModel()
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun loadDataFromViewModel() {
        viewModel.injectDataFromFirebase()
    }

    private fun setUpActionBar() {
        (activity as BaseActivity).setupActionBar(binding.actionBar)
        (activity as BaseActivity).setBackButtonVisibility(false)
        (activity as BaseActivity).setSearchIconvisibility(true)
        (activity as BaseActivity).setMessageIconvisibility(true)
    }

}