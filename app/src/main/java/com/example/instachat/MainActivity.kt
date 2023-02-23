package com.example.instachat

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.instachat.databinding.ActivityMainBinding
import com.example.instachat.utils.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupBottomNavigation()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.roomSyncRepository.commentsDao
    }

    private fun setupBottomNavigation() {
        val navGraphIds = listOf<Int>(
            R.navigation.nav_graph_home,
            R.navigation.nav_graph_search,
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

    fun showSnackBar(body: String){
        Snackbar.make(binding.root, body, Snackbar.LENGTH_LONG).setAction("Dismiss"){
        }.setBackgroundTint(Color.WHITE).setActionTextColor(Color.BLUE).setTextColor(Color.BLACK).show()
    }

}


