package com.androidians.lightstory

import android.os.Build
import java.util.*

object Utils {

    const val DURATION_OF_STORY_MINUTES = 1440 // 1 day in minutes
    const val FOLDER_NAME_IMAGES = "LightStory"
    const val AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider"
    val FILENAME_CAMERA_IMAGE = "Title_" + Calendar.getInstance().time.time + ".jpeg"


    private fun isBuildVersionMOrGreater(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }


}