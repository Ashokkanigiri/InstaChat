package com.example.instachat.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.example.instachat.databinding.LayoutNetworkDialogBinding

object DialogUtils {

    fun populateConnectivityErrorDialog(context: Context){
        val binding = LayoutNetworkDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        binding.ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }
}