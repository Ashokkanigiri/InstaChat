package com.example.instachat

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.instachat.databinding.ActivityMainBinding
import com.example.instachat.utils.setupWithNavController
import com.google.android.material.snackbar.Snackbar
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

    fun setBottomNavVisibility(bottomNavVisibility: Boolean){
        if(bottomNavVisibility){
            binding.bottomNav.visibility = View.VISIBLE
        }else{
            binding.bottomNav.visibility = View.GONE
        }
    }

    fun showSnackBar(){
        Snackbar.make(binding.root, "Test", Snackbar.LENGTH_INDEFINITE).setAction("Dismiss"){

        }.setBackgroundTint(Color.WHITE).setActionTextColor(Color.BLUE).show()
    }

}


