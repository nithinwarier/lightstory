package com.androidians.lightstory.data

import android.content.Context

open class LocalRepository(context: Context)
    : DefaultSharedPreferences(context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)) {

    fun putStoryTimestamp(timestamp: Long) = putLong(SHARED_PREFERENCE_STORY_TIMESTAMP, timestamp)
    fun getStoryTimestamp() = getLong(SHARED_PREFERENCE_STORY_TIMESTAMP)

    fun putCapturedImagePath(path: String) = putString(SHARED_PREFERENCE_CAMERA_CAPTURED_IMAGE_PATH, path)
    fun getCapturedImagePath() = getString(SHARED_PREFERENCE_CAMERA_CAPTURED_IMAGE_PATH)

    fun clear() = clearAll()

}