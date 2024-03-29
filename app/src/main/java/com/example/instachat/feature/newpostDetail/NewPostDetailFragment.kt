package com.example.instachat.feature.newpostDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentNewPostBinding
import com.example.instachat.databinding.FragmentNewPostDetailBinding
import com.example.instachat.utils.ConstantUtils
import com.example.instachat.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewPostDetailFragment : Fragment() {

    private var _binding: FragmentNewPostDetailBinding? = null
    private val binding get() = _binding!!

    val viewModel: NewPostDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_post_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
        setUpToolBar()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NewPostDetailViewModelEvent.IsPostAdded -> {
                    (activity as MainActivity).listenToNewPostWorkId(it.workId)
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }
                is NewPostDetailViewModelEvent.ShouldShowNetworkConnectionDialog ->{
                    DialogUtils.populateConnectivityErrorDialog(requireContext())
                }

            }
        })
    }

    private fun setUpToolBar() {
        binding.layoutHeader.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initFragment() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.post = viewModel.newPost
        arguments?.getStringArray("selectedAndCapturedImages")?.let {
            viewModel.adapter.submitList(it.toList().map { it.toUri() })
            viewModel.selectedImagesList = it.toList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}