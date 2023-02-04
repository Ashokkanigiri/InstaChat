package com.example.instachat.comment

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
import com.example.instachat.databinding.FragmentCommentBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommentFragment : Fragment() {

    lateinit var binding: FragmentCommentBinding
    val viewModel: CommentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        arguments?.getInt("postId")?.let {
            loadComments(it)
        }
        setUpActionBar()

    }

    fun loadComments(postId: Int){
        viewModel.roomRepository.commentsDao.getAllCommentsForPost(postId).observe(viewLifecycleOwner, Observer {
            viewModel.setAdapter().submitList(it)
        })
        viewModel.loadCurrentPost(postId)
    }

    private fun setUpActionBar() {
        (activity as BaseActivity).setupActionBar(binding.actionBar)
        (activity as BaseActivity).setBackButtonVisibility(true)
        (activity as BaseActivity).setSearchIconvisibility(false)
        (activity as BaseActivity).setMessageIconvisibility(false)
        (activity as BaseActivity).handleBackPressed(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if(viewModel.isCommentUpdated){
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("Should_refresh_post", true)
                }
                findNavController().popBackStack()
            }

        })


    }


}