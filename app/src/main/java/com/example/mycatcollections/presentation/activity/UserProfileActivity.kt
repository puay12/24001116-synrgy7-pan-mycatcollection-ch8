package com.example.mycatcollections.presentation.activity

import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import coil.load
import com.example.mycatcollections.R
import com.example.mycatcollections.databinding.ActivityUserProfileBinding
import com.example.mycatcollections.presentation.viewmodel.UserProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class UserProfileActivity : AppCompatActivity() {
    companion object {
        const val KEY_IMAGE_URI = "KEY_IMAGE_URI"

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, UserProfileActivity::class.java))
        }
    }

    private val binding by lazy { ActivityUserProfileBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<UserProfileViewModel>()
    private lateinit var imageUri: Uri
    private var permissionCheckLogic: Boolean = false

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        ::handlePermissionResult
    )

    private val fromGalleryResult = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ::handleGalleryResult
    )

    private val fromCameraResult = registerForActivityResult(
        ActivityResultContracts.TakePicture(),
        ::handleCameraResult
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.uploadBtn.setOnClickListener {
            checkPermissionLogic()

            if (permissionCheckLogic) {
                showImageDialog()
            } else {
                askForPermission()
            }
        }

        binding.goBtn.setOnClickListener {
            viewModel.applyBlur(blurLevel)
        }

        binding.cancelButton.setOnClickListener {
            viewModel.cancelWork()
        }

        viewModel.outputWorkInfos.observe(this, workInfosObserver())
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id: Int = item.itemId
//
//        if (id == androidx.appcompat.R.id.home) {
//            this.finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    private fun checkPermissionLogic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionCheckLogic = (
                isMediaImagePermissionGranted() &&
                isCameraPermissionGranted() &&
                isNotifPermissionGranted()
            )
        } else {
            permissionCheckLogic = (
                isCameraPermissionGranted() &&
                isReadExternalStoragePermissionGranted() &&
                isWriteExternalStoragePermissionGranted()
            )
        }
    }

    // PERMISSIONS
    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionRequest.launch(
                arrayOf(
                    CAMERA,
                    READ_MEDIA_IMAGES,
                    POST_NOTIFICATIONS
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionRequest.launch(
                arrayOf(
                    CAMERA,
                    READ_EXTERNAL_STORAGE
                )
            )
        } else {
            permissionRequest.launch(
                arrayOf(
                    CAMERA,
                    WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    private fun isWriteExternalStoragePermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
    }

    private fun isReadExternalStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, CAMERA) == PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isMediaImagePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES) == PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isNotifPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PERMISSION_GRANTED
    }

    private fun handlePermissionResult(result: Map<String, Boolean>) {
        val checkValue: Boolean = result.containsValue(false)

        if (checkValue) {
            Toast.makeText(this, "Permission is denied", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show()
        }
    }

    // UPLOAD IMAGE
    private fun showImageDialog() {
        AlertDialog.Builder(this)
            .setMessage("Choose One")
            .setPositiveButton("Gallery") { _,_ -> openGallery() }
            .setNegativeButton("Camera") { _,_ -> openCamera() }
            .show()
    }

    private fun openGallery() {
        intent.type = "image/*"
        fromGalleryResult.launch("image/*")
    }

    private fun handleGalleryResult(result: Uri?) {
        viewModel.setImageUri(result!!)
        binding.avatar.setImageURI(result)
        makeBlurFeatureVisible()
    }

    private fun openCamera() {
        val pict = File.createTempFile(
            "PICT_",
            ".jpg",
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        imageUri = FileProvider.getUriForFile(
            this,
            "com.example.mycatcollections.provider",
            pict
        )

        fromCameraResult.launch(imageUri)
    }

    private fun handleCameraResult(result: Boolean) {
        if (result) {
            viewModel.setImageUri(imageUri)
            binding.avatar.load(imageUri)
            makeBlurFeatureVisible()
        }
    }

    private fun makeBlurFeatureVisible() {
        binding.goBtn.visibility = View.VISIBLE
        binding.radioBlurGroup.visibility = View.VISIBLE
    }

    // BLUR IMAGE
    private fun workInfosObserver(): Observer<List<WorkInfo>> {
        return Observer { listOfWorkInfo ->

            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.

            // If there are no matching work info, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@Observer
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished) {
                showWorkFinished()

                // Normally this processing, which is not directly related to drawing views on
                // screen would be in the ViewModel. For simplicity we are keeping it here.
                val outputImageUri = workInfo.outputData.getString(KEY_IMAGE_URI)

                // If there is an output file show "See File" button
                if (!outputImageUri.isNullOrEmpty()) {
                    viewModel.setOutputUri(outputImageUri)
                    binding.avatar.load(outputImageUri)
                }
            } else {
                showWorkInProgress()
            }
        }
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private fun showWorkInProgress() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            goBtn.visibility = View.GONE
        }
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private fun showWorkFinished() {
        with(binding) {
            progressBar.visibility = View.GONE
            cancelButton.visibility = View.GONE
            goBtn.visibility = View.VISIBLE
        }
    }

    private val blurLevel: Int
        get() =
            when (binding.radioBlurGroup.checkedRadioButtonId) {
                R.id.radio_blur_lv_1 -> 1
                R.id.radio_blur_lv_2 -> 2
                R.id.radio_blur_lv_3 -> 3
                else -> 1
            }
}