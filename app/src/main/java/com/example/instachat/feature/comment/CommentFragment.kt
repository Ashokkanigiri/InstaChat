package com.example.instachat.feature.comment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instachat.BaseActivity
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentCommentBinding
import com.example.instachat.generated.callback.OnClickListener
import com.example.instachat.utils.ConstantUtils
import com.example.instachat.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommentFragment : Fragment() {


    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
        loadArguments()
        setUpActionBar()
        loadViewModel()
    }

    private fun initFragment(){
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        if (!viewModel.adapter.hasObservers()) {
            viewModel.adapter.setHasStableIds(true)
        }
        binding.rvComments.adapter = viewModel.adapter
    }

    private fun loadArguments() {
        arguments?.getInt(ConstantUtils.NavKeys.POST_ID_KEY)?.let {
            viewModel.loadData(it)
        }
    }

    private fun loadViewModel() {
        viewModel.getCurrentPostEvent.observe(viewLifecycleOwner, Observer {
            binding.post = it
            viewModel.loadCurrentPostedUser(it?.userId)
        })

        viewModel.getPostedUserEvent.observe(viewLifecycleOwner, Observer {
            binding.postedUser = it
        })

        viewModel.loggedUserEvent.observe(viewLifecycleOwner, Observer {
            binding.currentUser = it
        })

        viewModel.onPostedCommentEvent.observe(viewLifecycleOwner, Observer {
            binding.root.hideKeyboard()
        })
    }

    private fun setUpActionBar() {
        (activity as BaseActivity).setupActionBar(binding.actionBar)
        (activity as BaseActivity).setBackButtonVisibility(true)
        (activity as BaseActivity).setAddPostIconVisibility(false)
        (activity as BaseActivity).setMessageIconvisibility(false)
        (activity as MainActivity).setBottomNavVisibility(false)
        (activity as MainActivity).handleBackPressed {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}