package com.example.instachat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.instachat.databinding.ActivityMainBinding
import com.example.instachat.databinding.LayoutLoginActivityBinding
import com.example.instachat.services.sharedprefs.SharedPreferenceService
import com.example.instachat.utils.DialogUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: LayoutLoginActivityBinding? = null
    private val binding get() = _binding!!

    val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = Firebase.auth

        if (auth.currentUser != null) {
            navigateToMainActivity()
        }

        _binding = DataBindingUtil.setContentView(this, R.layout.layout_login_activity)
        binding.lifecycleOwner = this

        listenToFCMToken()
        observeViewModel()

    }

    fun listenToFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            val session = SharedPreferenceService.getUniqueSessionId(this)
            if(session == null){
                SharedPreferenceService.putUniqueSessionId(this, UUID.randomUUID().toString())
            }
            session?.let {
                viewModel.updateSessionIdToCurrentUser(session, token)
            }
            Log.d("jfqljnfqlkfn", "${token}")
        })

    }

    private fun navigateToMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

    private fun observeViewModel() {
        viewModel.showConnectivityErrorDialog.observe(this){
            DialogUtils.populateConnectivityErrorDialog(this)
        }
        viewModel.navigateToMainActivity.observe(this){
            navigateToMainActivity()
        }
        viewModel.showNetworkConnectivityDialog.observe(this, Observer {
            DialogUtils.populateConnectivityErrorDialog(this)
        })
        viewModel.handleError.observe(this, Observer {
            showSnackbar(it)
        })
        viewModel.updateUserWorkId.observe(this, Observer {
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(it).observe(this, Observer {workInfo->
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                       navigateToMainActivity()
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
        })
    }

    private fun showSnackbar(message: String){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}