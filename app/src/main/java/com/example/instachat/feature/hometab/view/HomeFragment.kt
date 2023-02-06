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
import com.example.instachat.R
import com.example.instachat.databinding.FragmentHomeBinding
import com.example.instachat.feature.hometab.HomeViewModelEvent
import com.example.instachat.feature.hometab.models.HomeDataModel
import com.example.instachat.feature.hometab.viewmodel.HomeViewModel
import com.example.instachat.utils.DialogUtils
import com.google.gson.Gson
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

        arguments?.getInt("postId")?.let {
            viewModel.selectedPostId = it
            viewModel.isFromSearchFragment = true
            binding.rvUsers.visibility= View.GONE
        }
    }

    private fun observeViewModel() {

        viewModel.event.observe(viewLifecycleOwner, Observer {
            when(it){
                is HomeViewModelEvent.ShowConnectivityErrorDialog ->{
                    DialogUtils.populateConnectivityErrorDialog(requireActivity())
                }
            }
        })

        if(viewModel.isFromSearchFragment){
            viewModel.roomRepository.postsDao.getAllPostsHomeData()
                .observe(viewLifecycleOwner, Observer {
                    val list = it
                    val clickedPost = list.filter { it.postId == viewModel.selectedPostId }.first()
                    list?.toMutableList()?.remove(clickedPost)
                    val finallist: List<HomeDataModel> = listOf(clickedPost)+list
                    viewModel.adapter.submitList(finallist)
                })
        }else{
            viewModel.roomRepository.usersDao.getallUsers().observe(viewLifecycleOwner, Observer {
                viewModel.usersAdapter.submitList(it)
            })

            viewModel.roomRepository.postsDao.getPostsHomeDataLive("12")
                .observe(viewLifecycleOwner, Observer {
                    viewModel.adapter.submitList(it)
                })

        }

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
        if (viewModel.isFromSearchFragment) {
            (activity as BaseActivity).setBackButtonVisibility(true)
            (activity as BaseActivity).setSearchIconvisibility(false)
            (activity as BaseActivity).setMessageIconvisibility(false)
            (activity as BaseActivity).setBackLabelText("Explore")
            (activity as BaseActivity).handleBackPressed(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    findNavController().popBackStack()
                }

            })
        } else {
            (activity as BaseActivity).setBackButtonVisibility(false)
            (activity as BaseActivity).setSearchIconvisibility(true)
            (activity as BaseActivity).setMessageIconvisibility(true)

        }
    }
}