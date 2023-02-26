package com.example.instachat.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.LayoutInflater
import com.example.instachat.R
import com.example.instachat.databinding.LayoutLogoutDialogBinding
import com.example.instachat.databinding.LayoutNetworkDialogBinding
import com.example.instachat.databinding.LayoutPermissionDialogBinding
import com.example.instachat.databinding.LayoutProgressDialogBinding
import com.example.instachat.databinding.LayoutUserExistsBinding


object DialogUtils {

    fun populateConnectivityErrorDialog(context: Context) {
        val binding = LayoutNetworkDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        binding.button2.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    fun populatePermissionDialog(
        context: Context,
        okayButtonListener: (() -> Unit),
        descriptionText: String
    ) {
        val binding = LayoutPermissionDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        binding.btnOkay.setOnClickListener {
            dialog.dismiss()
            okayButtonListener.invoke()
        }
        binding.btnNoIamGood.setOnClickListener {
            dialog.dismiss()
        }
        binding.tvDes.text = descriptionText
        dialog.setCancelable(false)
        dialog.show()
    }

    fun populateUserExistsDialog(
        context: Context
    ) {
        val binding = LayoutUserExistsBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        binding.btnOkay.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    fun populateLogoutDialog(
        context: Context,
        handleLogoutClicked: (() -> Unit)
    ) {
        val binding = LayoutLogoutDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        binding.btnLogout.setOnClickListener {
            dialog.dismiss()
            handleLogoutClicked.invoke()
        }
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    fun populateProgressDialog(
        context: Context,
        shouldShowProgress: Boolean
    ) {
        val binding = LayoutProgressDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent)));

        if(shouldShowProgress){
            dialog.show()
        }else{
            dialog.dismiss()
        }
    }
}