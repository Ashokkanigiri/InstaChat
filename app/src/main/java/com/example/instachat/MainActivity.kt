package com.example.instachat

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.instachat.databinding.ActivityMainBinding
import com.example.instachat.utils.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by viewModels()
    lateinit var networkSnackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        intent.extras?.getBoolean("IS_FROM_NOTIFICATION")?.let {
            if(it){
                viewModel.injectAllNotifications()
            }
        }

        viewModel.listenToNetworkConnection()
        initNetworkSnackBar()
        setupBottomNavigation()
        observeViewModel()
        Log.d("jflqnflqn", "kfnqfnql: MAin")
        testFun()
    }

    private fun testFun() {
    }

    private fun observeViewModel() {
        viewModel.shouldShowNetworkConnectionErrorSnackBar.observe(this, Observer {
            if (it) {
                networkSnackBar.show()
            } else {
                networkSnackBar.dismiss()
            }
        })
    }

    fun initNetworkSnackBar() {
        networkSnackBar = Snackbar.make(
            binding.root,
            "Unable to connect to Internet, please check your Internet connection & try again",
            Snackbar.LENGTH_INDEFINITE
        ).setBackgroundTint(Color.WHITE).setTextColor(resources.getColor(R.color.light_red))
    }

    private fun setupBottomNavigation() {
        val navGraphIds = listOf<Int>(
            R.navigation.nav_graph_home,
            R.navigation.nav_graph_search,
            R.navigation.nav_graph_notifications,
            R.navigation.nav_graph_settings
        )
        binding.bottomNav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.container,
            intent = intent
        )
    }

    fun setBottomNavVisibility(bottomNavVisibility: Boolean) {
        if (bottomNavVisibility) {
            binding.bottomNav.visibility = View.VISIBLE
        } else {
            binding.bottomNav.visibility = View.GONE
        }
    }

    fun listenToNewPostWorkId(workId: UUID) {
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workId).observe(this, Observer {
            when (it.state) {
                WorkInfo.State.SUCCEEDED -> {
                    showSnackBar("Post Uploaded successfully")
                }
                WorkInfo.State.BLOCKED -> {

                }
                WorkInfo.State.CANCELLED -> {
                    showSnackBar("Error Occurred while uploading post !")
                }
                WorkInfo.State.ENQUEUED -> {

                }
                WorkInfo.State.FAILED -> {
                    showSnackBar("Error Occurred while uploading post !")
                }
                WorkInfo.State.RUNNING -> {
                    showSnackBar("Uploading Post ...")
                }
            }
        })
    }

    fun showSnackBar(body: String) {
        Snackbar.make(binding.root, body, Snackbar.LENGTH_LONG).setAction("Dismiss") {
        }.setBackgroundTint(Color.WHITE).setActionTextColor(Color.BLUE).setTextColor(Color.BLACK)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}


