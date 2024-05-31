package com.example.mycatcollections.presentation.activity

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.load
import com.example.mycatcollections.R
import com.example.mycatcollections.databinding.ActivityUserProfileBinding
import java.io.File

class UserProfileActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, UserProfileActivity::class.java))
        }
    }

    private lateinit var imageUri: Uri
    private var permissionCheckLogic: Boolean = false
    private val binding by lazy { ActivityUserProfileBinding.inflate(layoutInflater) }

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionCheckLogic = (isMediaImagePermissionGranted() && isCameraPermissionGranted())
        } else {
            permissionCheckLogic = (
                isCameraPermissionGranted() &&
                isReadExternalStoragePermissionGranted() &&
                isWriteExternalStoragePermissionGranted()
            )
        }

        binding.uploadBtn.setOnClickListener {
            if (permissionCheckLogic) {
                showImageDialog()
            } else {
                askForPermission()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId

        if (id == androidx.appcompat.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item)
    }

    // PERMISSIONS
    private fun askForPermission() {
        permissionRequest.launch(
            arrayOf(
                CAMERA,
                READ_MEDIA_IMAGES,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
        )
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

    private fun handlePermissionResult(result: Map<String, Boolean>) {
        Log.i("hasil permisi", result.toString())
        if (result.containsValue(false)) {
            Toast.makeText(this, "Permission is denied", Toast.LENGTH_SHORT).show()
            onBackPressed()
        } else {
            Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show()
        }
    }

    // IMAGES
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
        binding.avatar.setImageURI(result)
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
            binding.avatar.load(imageUri)
        }
    }
}