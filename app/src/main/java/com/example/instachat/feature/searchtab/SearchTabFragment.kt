package com.example.instachat.feature.searchtab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instachat.R
import com.example.instachat.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchTabFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    val viewModel: SearchTabViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.roomRepository.postsDao.getAllPosts().observe(viewLifecycleOwner, Observer {
            viewModel.adapter.submitList(it)
        })

        viewModel.event.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(
                SearchTabFragmentDirections.actionSearchTabFragmentToHomeFragment(it)
            )
        })
    }

    private fun initFragment() {
        binding.lifecycleOwner = viewLifecycleOwner
        if(!viewModel.adapter.hasObservers()) viewModel.adapter.setHasStableIds(true)
        val layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.rvSearchTab.layoutManager = layoutManager
        binding.rvSearchTab.adapter = viewModel.adapter
    }

}