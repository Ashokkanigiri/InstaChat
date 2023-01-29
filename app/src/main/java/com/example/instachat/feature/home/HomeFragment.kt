package com.example.instachat.feature.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instachat.R
import com.example.instachat.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

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
        loadDataFromViewModel()
        handleSwipeLayout()
        handleViewModelEvents()
    }

    /**
     * TODO: NEED TO REFRESH ONLY THE MODIFIED ITEM
     *
     * this is used to refresh the page after entering the comment
     */
    override fun onResume() {
        super.onResume()
        viewModel.getAllDataFromFirebase()
    }

    private fun handleViewModelEvents() {
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
            viewModel.getAllDataFromFirebase()
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun loadDataFromViewModel() {
        viewModel.getAllDataFromFirebase()
    }

    private fun initFragment() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

}