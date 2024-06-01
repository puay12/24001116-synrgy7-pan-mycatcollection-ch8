package com.example.mycatcollections.worker.blurimage

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.mycatcollections.presentation.activity.UserProfileActivity.Companion.KEY_IMAGE_URI

class BlurImageWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {
    companion object {
        private const val TAG = "BlurImageWorker"
    }

    override fun doWork(): Result {
        val appContext = applicationContext

        val inputUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", appContext)

        // This is an utility function added to emulate slower work.
        sleep()

        return try {
            if (TextUtils.isEmpty(inputUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(inputUri)))

            val output = blurBitmap(picture, appContext)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)

            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(outputData)
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            throwable.printStackTrace()
            Result.failure()
        }
    }

}