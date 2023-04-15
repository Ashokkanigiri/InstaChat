package com.example.instachat.feature.settingstab

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
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.instachat.LoginActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentSettingsBinding
import com.example.instachat.services.sharedprefs.SharedPreferenceService
import com.example.instachat.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding

    val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragment()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SettingsViewModelEvent.HandleLogoutButtonClicked -> {
                    onLogoutClicked()
                }
                is SettingsViewModelEvent.NavigateToUserDetailFragment -> {
                    navigateToUserDetail(it.userId)
                }
                is SettingsViewModelEvent.LoadUserDetails -> {
                    binding.user = it.user
                }
                is SettingsViewModelEvent.ListenToRegistrationTokenDeleteWorkId -> {
                    listenToRegistrationTokenWorkId(it.uuid)
                }
                is SettingsViewModelEvent.HandleError ->{
                    showSnackbar(it.des)
                }
                is SettingsViewModelEvent.ShowConnectivityErrorDialog->{
                    DialogUtils.populateConnectivityErrorDialog(requireContext())
                }
            }
        })
    }

    private fun listenToRegistrationTokenWorkId(workId: UUID) {
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workId)
            .observe(viewLifecycleOwner, Observer { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        clearAllDatabases()
                    }
                    WorkInfo.State.BLOCKED -> {

                    }
                    WorkInfo.State.CANCELLED -> {
                        showSnackbar("Unknown Error occurred, please try again")
                    }
                    WorkInfo.State.ENQUEUED -> {

                    }
                    WorkInfo.State.FAILED -> {
                        showSnackbar("Unknown Error occurred, please try again")
                    }
                    WorkInfo.State.RUNNING -> {

                    }
                }
            })
    }

    private fun navigateToUserDetail(userId: String) {
        findNavController().navigate(
            SettingsFragmentDirections.actionSettingsFragmentToUserDetailsFragment3(userId)
        )
    }

    private fun onLogoutClicked() {
        DialogUtils.populateLogoutDialog(requireContext()) {
            viewModel.clearCurrentSession(SharedPreferenceService.getUniqueSessionId(requireContext()))
        }
    }

    private fun clearAllDatabases() {
        viewModel.clearAllDatabases()
        SharedPreferenceService.clearAllKeys(requireContext())
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(activity, LoginActivity::class.java))
        this.activity?.finish()
    }

    private fun initFragment() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.loadUserDetails()
    }

    private fun showSnackbar(message: String){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }


}