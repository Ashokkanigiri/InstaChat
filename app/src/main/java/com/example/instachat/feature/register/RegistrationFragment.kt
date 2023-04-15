package com.example.instachat.feature.register

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.instachat.LoginActivity
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentRegistrationBinding
import com.example.instachat.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    lateinit var binding: FragmentRegistrationBinding

    val viewModel: RegistrationViewModel by viewModels()

    lateinit var progressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initFragment()
        observeViewModel()
    }

    private fun initFragment() {
        progressDialog = DialogUtils.getProgressDialog(requireContext(), Dialog(requireContext()))
        handleGenderRadioGroup()
    }

    private fun handleGenderRadioGroup() {
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton: RadioButton? = radioGroup?.findViewById<RadioButton>(i)
            viewModel.user.gender = radioButton?.text?.trim()?.toString()?:""
        }
    }

    private fun observeViewModel() {
        viewModel.event.observe(viewLifecycleOwner, Observer {
            when(it){
                is RegistrationViewModelEvent.PopulateUserAlreadyExistsDialog ->{
                    DialogUtils.populateUserExistsDialog(requireContext())
                }
                is RegistrationViewModelEvent.HandleRegistrationSuccess ->{
                    (activity as LoginActivity).listenToFCMToken()
                }
                is RegistrationViewModelEvent.PopulateConnectivityErrorDialog ->{
                    DialogUtils.populateConnectivityErrorDialog(requireContext())
                }
                is RegistrationViewModelEvent.NavigateBackToLoginScreen ->{
                    findNavController().popBackStack()
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

}