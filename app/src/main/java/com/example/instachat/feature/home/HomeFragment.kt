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
import androidx.navigation.fragment.findNavController
import com.example.instachat.BaseActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentHomeBinding
import com.example.instachat.services.repository.RoomRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var roomRepository: RoomRepository

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

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("Should_refresh_post")?.observe(viewLifecycleOwner) { shouldRefreshPost ->
            if(shouldRefreshPost){
                viewModel.refreshPost()
            }
        }
    }

    private fun setUpActionBar() {
        (activity as BaseActivity).setupActionBar(binding.actionBar)
        (activity as BaseActivity).setBackButtonVisibility(false)
        (activity as BaseActivity).setSearchIconvisibility(true)
        (activity as BaseActivity).setMessageIconvisibility(true)
    }

    private fun observeViewModel() {
        viewModel.commentsLayoutClickedEvent.observe(viewLifecycleOwner, Observer {postId->
            postId?.let {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToCommentFragment(it)
                )
            }
        })

        roomRepository.postsDao.getAllPosts().observe(viewLifecycleOwner, Observer {
            viewModel.adapter.submitList(it)
        })

        roomRepository.usersDao.getallUsers().observe(viewLifecycleOwner, Observer {
            viewModel.usersAdapter.submitList(it)
        })
    }

    private fun handleSwipeLayout() {
        binding.swipeLayout.setOnRefreshListener {
            loadDataFromViewModel()
            roomRepository.postsDao.getAllPosts().observe(viewLifecycleOwner, Observer {
                viewModel.adapter.submitList(it)
            })
            roomRepository.usersDao.getallUsers().observe(viewLifecycleOwner, Observer {
                viewModel.usersAdapter.submitList(it)
            })
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun loadDataFromViewModel() {
        viewModel.injectDatabases()
    }

    private fun initFragment() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

}