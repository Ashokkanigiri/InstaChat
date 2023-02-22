package com.example.instachat.feature.newpost

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentNewPostBinding
import com.example.instachat.utils.DialogUtils
import com.example.instachat.utils.StorageUtils
import com.google.common.util.concurrent.ListenableFuture
import com.google.gson.Gson
import java.io.File


class NewPostFragment : Fragment() {


    lateinit var binding: FragmentNewPostBinding
    lateinit var imageCapture: ImageCapture
    lateinit var camera: Camera

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.map { it.value }.contains(false)) {
                populateEducationalDialog()
            } else {
                initCamera()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_post, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
        checkForCameraAndStoragePermissions()
    }

    private fun isPermissionsGranted(): Boolean {
        val isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val isReadStoragePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val isWriteStoragePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return isCameraPermissionGranted && isReadStoragePermissionGranted && isWriteStoragePermissionGranted
    }

    private fun checkForCameraAndStoragePermissions() {
        when {
            isPermissionsGranted() -> {
                initCamera()
            }
            else -> {
                requestPermissionLauncher.launch(permissions)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionsGranted()) initCamera()
    }

    private fun initCamera() {
        binding.tvPermissionError.visibility = View.GONE
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))

        binding.ivCaptureImage.setOnClickListener {
            captureImage()
        }
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        imageCapture = ImageCapture.Builder().setFlashMode(FLASH_MODE_ON).build()

        cameraProvider.unbindAll()

        camera = cameraProvider.bindToLifecycle(
            viewLifecycleOwner,
            cameraSelector,
            preview,
            imageCapture,
        )

    }

    private fun openSettingsScreen() {
        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.setData(uri)
        requireContext().startActivity(intent)
    }


    private fun captureImage() {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(File("")).build()
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    // insert your code here.
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // insert your code here.
                }
            })
    }


    private fun initFragment() {
        populateActionBarFromSearch()
        binding.tvPermissionError.setOnClickListener {
            openSettingsScreen()
        }
    }

    private fun populateEducationalDialog() {
        DialogUtils.populatePermissionDialog(
            requireContext(),
            okayButtonListener = {
                binding.tvPermissionError.visibility = View.VISIBLE
            },
            descriptionText = resources.getString(R.string.permission_des_text)
        )
    }

    private fun populateActionBarFromSearch() {
        (activity as MainActivity).setBottomNavVisibility(false)
        binding.layoutHeader.imageView13.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}