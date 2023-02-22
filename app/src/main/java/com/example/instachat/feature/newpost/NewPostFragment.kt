package com.example.instachat.feature.newpost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instachat.MainActivity
import com.example.instachat.R
import com.example.instachat.databinding.FragmentNewPostBinding
import com.example.instachat.utils.DialogUtils
import com.example.instachat.utils.StorageUtils
import com.example.instachat.utils.StorageUtils.isPermissionsGranted
import com.google.common.util.concurrent.ListenableFuture
import com.google.gson.Gson
import java.io.File


class NewPostFragment : Fragment() {


    lateinit var binding: FragmentNewPostBinding
    lateinit var imageCapture: ImageCapture
    lateinit var camera: Camera
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    val viewModel : NewPostViewModel by viewModels()

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
        loadViewModel()
    }

    private fun loadViewModel() {
        viewModel.event.observe(viewLifecycleOwner, Observer {
            when(it){
                is NewPostViewModelEvent.LoadImagesFromGallery->{
                    viewModel.adapter.submitList(it.imagesList)
                }
            }
        })
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.map { it.value }.contains(false)) {
                populateEducationalDialog()
            } else {
                initCamera()
                viewModel.getImages(requireActivity())
            }
        }


    private fun checkForCameraAndStoragePermissions() {
        when {
            requireActivity().isPermissionsGranted() -> {
                loadCameraAndImages()
            }
            else -> {
                requestPermissionLauncher.launch(StorageUtils.PERMISSIONS)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity().isPermissionsGranted()) {
            loadCameraAndImages()
        }
    }

    private fun loadCameraAndImages(){
        initCamera()
        viewModel.getImages(requireContext())
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

        imageCapture = ImageCapture.Builder().setFlashMode(FLASH_MODE_OFF).build()

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
        val path = requireContext().filesDir.absolutePath+"/${System.currentTimeMillis()}.jpg"

        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(File(path)).build()
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Error while capturing image, please try again", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let {
                        viewModel.addImageToSelectedAndCapturedList(it.toString())
                    }
                }
            })
    }


    private fun initFragment() {
        populateActionBarFromSearch()
        binding.viewModel = viewModel
        binding.tvPermissionError.setOnClickListener {
            openSettingsScreen()
        }
        if (!viewModel.adapter.hasObservers()) viewModel.adapter.setHasStableIds(true)
        binding.rvGalleryImages.layoutManager = GridLayoutManager(requireContext(), 5, GridLayoutManager.VERTICAL, false)
        binding.rvGalleryImages.adapter = viewModel.adapter
        binding.layoutHeader.ivNext.setOnClickListener {
            navigateToNewPostDetailFragment()
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

    private fun navigateToNewPostDetailFragment(){
        val list : Array<String> = viewModel.getSelectedAndCapturedList().toTypedArray()
        findNavController().navigate(
            NewPostFragmentDirections.actionNewPostFragmentToNewPostDetailFragment(list)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.getSelectedAndCapturedList().clear()
    }

}