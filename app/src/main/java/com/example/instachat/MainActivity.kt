package com.example.instachat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.instachat.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

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
        val navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        setupWithNavController(binding.bottomNav, navHost.navController)
    }

}


