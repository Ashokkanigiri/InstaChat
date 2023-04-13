package com.example.instachat.feature.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentLoginBinding
import com.example.instachat.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    val viewModel: LoginViewModel by viewModels()

    lateinit var progressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        progressDialog = DialogUtils.getProgressDialog(requireContext(), Dialog(requireContext()))

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoginViewModelEvent.NavigateToHomeScreen -> {
                    navigateToHomeScreen()
                }
                LoginViewModelEvent.NavigateToRegistrationScreen -> {
                    navigateToRegistrationScreen()
                }
            }
        })
        viewModel.progressBarEvent.observe(viewLifecycleOwner, Observer {
            if(it){
                progressDialog.show()
            }else{
                progressDialog.dismiss()
            }
        })
    }

    private fun navigateToHomeScreen() {
        startActivity(Intent(this.activity, MainActivity::class.java))
        this.activity?.finish()
    }

    private fun navigateToRegistrationScreen() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
        )
    }

}