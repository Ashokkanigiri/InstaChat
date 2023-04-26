package com.example.instachat.feature.userdetails

import android.graphics.Color
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
import com.example.instachat.databinding.FragmentSettingsBinding
import com.example.instachat.databinding.FragmentUserDetailsBinding
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.RequestedForInterestModel
import com.example.instachat.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    val viewModel: UserDetailViewModel by viewModels()
    lateinit var errorSnackBar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
        observeViewModel()
        initNetworkSnackBar()
    }

    private fun observeViewModel() {
        viewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserDetailViewModelEvent.LoadUser -> {
                    binding.user = it.user
                    setUpToolBarName(it.user.username)
                    viewModel.loadAllPostsForUser(viewModel.userId?:"")
                }
                is UserDetailViewModelEvent.LoadPosts -> {
                    viewModel.adapter.submitList(it.posts)
                }
                UserDetailViewModelEvent.OnFollowButtonClicked -> {
                    viewModel.handleFollowButtonClicked()
                }
                UserDetailViewModelEvent.OnMessageButtonClicked -> {

                }
                is UserDetailViewModelEvent.OnFollowStatusRequested ->{

                }
                is UserDetailViewModelEvent.LoadLoggedUser ->{

                }
            }
        })

        viewModel.connectivityDialogEvent.observe(viewLifecycleOwner, Observer {
            DialogUtils.populateConnectivityErrorDialog(requireContext())
        })

        viewModel.handleError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(!errorSnackBar.isShown){
                    errorSnackBar.show()
                }
            }
        })
    }


    private fun setUpToolBarName(text: String){
        (activity as BaseActivity).setBackLabelText(text)

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

    fun initNetworkSnackBar() {
        errorSnackBar = Snackbar.make(
            binding.root,
            "Unknown error occurred, please try again after some time",
            Snackbar.LENGTH_INDEFINITE
        ).setBackgroundTint(Color.WHITE).setTextColor(resources.getColor(R.color.light_red))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setBottomNavVisibility(true)
        _binding = null
    }

    private fun initFragment() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setUpToolBar()
        if (!viewModel.adapter.hasObservers()) viewModel.adapter.setHasStableIds(true)
        val layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.rvPosts.layoutManager = layoutManager
        binding.rvPosts.adapter = viewModel.adapter

        arguments?.getString("userId")?.let {
            viewModel.apply {
                userId = it
                loadUser(it)
            }
        }
    }
}