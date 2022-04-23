package com.androidians.lightstory

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.androidians.lightstory.Utils.AUTHORITY
import com.androidians.lightstory.Utils.DURATION_OF_STORY_MINUTES
import com.androidians.lightstory.Utils.FILENAME_CAMERA_IMAGE
import com.androidians.lightstory.Utils.FOLDER_NAME_IMAGES
import com.androidians.lightstory.data.LocalRepository
import com.androidians.lightstory.databinding.ActivityMainBinding
import com.androidians.lightstory.ui.delayOnLifecycle
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var currentPhotoPath: String
    private lateinit var localRepository: LocalRepository

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            pickImageCamera(this)
        } else {
            Log.w(TAG, "user didn't give permission to launch camera")
        }
    }

    private var capturedImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            saveTimestampOfStory()
            addStory()
        } else {
            Log.w(TAG, "couldn't capture the image from camera, resultCode: ${result.resultCode}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localRepository = LocalRepository(this)
        setContentView(binding.root)
        binding.addStoryBtn.setOnClickListener {
            if (isBuildMarshMallowOrAbove()) {
                checkCameraPermission(this)
            }
        }
        showStory()
        binding.timeStampTV.delayOnLifecycle(1000L * 60) {
            showStory()
        }
    }

    private fun showStory() {
        val storyCreatedTimestamp = localRepository.getStoryTimestamp()
        currentPhotoPath = localRepository.getCapturedImagePath()
        if (storyCreatedTimestamp > 0
            && DateTimeUtils.getTimeDifferenceInMinutes(storyCreatedTimestamp,
                System.currentTimeMillis()) < DURATION_OF_STORY_MINUTES
        ) {
            showStoryView(true)
            showTimestampForStory()
        } else {
            showStoryView(false)
        }
    }

    private fun checkCameraPermission(context: Context) {
        val cameraPermission = arrayOf(Manifest.permission.CAMERA)
        if (isPermissionGranted(context, cameraPermission)) {
            pickImageCamera(context)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean {
        var flag = false
        for (permission in permissions) {
            if (isBuildMarshMallowOrAbove()) {
                flag = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return flag
    }

    private fun isBuildMarshMallowOrAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    private fun pickImageCamera(context: Context) {
        val output = File(File(this.filesDir, FOLDER_NAME_IMAGES), FILENAME_CAMERA_IMAGE)
        if (output.exists()) {
            output.delete()
        } else {
            Objects.requireNonNull(output.parentFile).mkdirs()
        }
        val contentUri = FileProvider.getUriForFile(context, AUTHORITY, output)
        currentPhotoPath = output.absolutePath
        localRepository.putCapturedImagePath(currentPhotoPath)
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
        capturedImageLauncher.launch(takePhotoIntent)
    }

    private fun addStory() {
        showStoryView(true)
        showTimestampForStory()
    }

    private fun saveTimestampOfStory() {
        localRepository.putStoryTimestamp(System.currentTimeMillis())
    }

    private fun showTimestampForStory() {
        val storyCreatedTimestamp = localRepository.getStoryTimestamp()
        val list = DateTimeUtils.getTimeDifferenceInHHmm(storyCreatedTimestamp, System.currentTimeMillis())
        Log.e(TAG, "todayOrYesterday ${list[0]}, hours: ${list[1]}, minutes: ${list[2]}")
        getTimeFormatToDisplay(list, storyCreatedTimestamp)
    }

    private fun getTimeFormatToDisplay(list: ArrayList<Long>, storyCreatedTimestamp: Long) {
        // Just now, 1 minute ago, 2 minutes ago ... (Today, 4.13 pm), (Yesterday, 4.13 pm or am)...
        val format = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        if (list[1] == 0L) {
            when (list[2]) {
                0L -> binding.timeStampTV.text = getString(R.string.just_now)
                1L -> binding.timeStampTV.text = getString(R.string._1_minute_ago)
                in 2L..59L -> binding.timeStampTV.text = getString(R.string.minutes_ago, list[2])
                else -> setDayFormat(list, format, storyCreatedTimestamp)
            }
        } else {
            setDayFormat(list, format, storyCreatedTimestamp)
        }
    }

    private fun setDayFormat(
        list: ArrayList<Long>,
        format: SimpleDateFormat,
        storyCreatedTimestamp: Long,
    ) {
        if (list[0] == 0L) {
            binding.timeStampTV.text = getString(R.string.day_timestamp,
                getString(R.string.yesterday),
                format.format(storyCreatedTimestamp))
        } else {
            binding.timeStampTV.text = getString(R.string.day_timestamp,
                getString(R.string.today),
                format.format(storyCreatedTimestamp))
        }
    }

    private fun showStoryView(show: Boolean) {
        if (show) {
            binding.lightStoryIV.setImageURI(Uri.fromFile(File(currentPhotoPath)))
            binding.timeStampTV.visibility = View.VISIBLE
            binding.addStoryBtn.visibility = View.GONE
        } else {
            binding.lightStoryIV.visibility = View.GONE
            binding.timeStampTV.visibility = View.GONE
            binding.addStoryBtn.visibility = View.VISIBLE
        }
    }

}
const val TAG = "MainActivity"