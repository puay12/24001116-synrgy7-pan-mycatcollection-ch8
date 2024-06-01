package com.example.mycatcollections.presentation.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.mycatcollections.R
import com.example.mycatcollections.extension.TAG_OUTPUT
import com.example.mycatcollections.presentation.activity.UserProfileActivity.Companion.KEY_IMAGE_URI
import com.example.mycatcollections.worker.blurimage.BlurImageWorker
import com.example.mycatcollections.worker.blurimage.CleanupWorker
import com.example.mycatcollections.worker.blurimage.SaveImageToFileWorker

class UserProfileViewModel(app: Application) : ViewModel() {
    companion object {
        private const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work_name"
    }

    private var imageUri: Uri? = null
    internal var imageOutputUri: Uri? = null
    private val workManager = WorkManager.getInstance(app)
    internal var outputWorkInfos: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)

    init {
        imageUri = getImageUri(app.applicationContext)
    }

    internal fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources

        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.ic_launcher_background))
            .appendPath(resources.getResourceTypeName(R.drawable.ic_launcher_background))
            .appendPath(resources.getResourceEntryName(R.drawable.ic_launcher_background))
            .build()
    }

    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    internal fun applyBlur(blurLevel: Int) {
        // Add WorkRequest to Cleanup temporary images
        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )

        // Add WorkRequests to blur the image the number of times requested
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurImageWorker>()

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataUri())
            }

            continuation = continuation.then(blurBuilder.build())
        }

        // Create charging constraint
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .build()

        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation.then(save)

        // Actually start the work
        continuation.enqueue()
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataUri(): Data {
        val builder = Data.Builder()

        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }

        return builder.build()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    internal fun setOutputUri(outputImageUri: String?) {
        imageOutputUri = uriOrNull(outputImageUri)
    }

    fun setImageUri(imageUri: Uri) {
        this.imageUri = imageUri
    }
}