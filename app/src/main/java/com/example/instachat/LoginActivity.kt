package com.example.instachat

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.instachat.databinding.LayoutLoginActivityBinding
import com.example.instachat.utils.DialogUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: LayoutLoginActivityBinding

    val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = Firebase.auth

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }

        binding = DataBindingUtil.setContentView(this, R.layout.layout_login_activity)
        binding.lifecycleOwner = this

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.showConnectivityErrorDialog.observe(this){
            DialogUtils.populateConnectivityErrorDialog(this)
        }
    }
}