package com.androidians.lightstory.data

import android.content.SharedPreferences

open class DefaultSharedPreferences(private val sharedPreferences: SharedPreferences) {

    companion object {
        const val DEFAULT_INTEGER = 0
        const val DEFAULT_LONG = 0L
        const val DEFAULT_STRING = ""
        const val DEFAULT_FLOAT = 0f
        const val DEFAULT_BOOLEAN = false

        const val PREFERENCE_NAME = "com.androidians"
        // preference keys
        const val SHARED_PREFERENCE_STORY_TIMESTAMP = "STORY_TIMESTAMP"
        const val SHARED_PREFERENCE_CAMERA_CAPTURED_IMAGE_PATH = "CAMERA_CAPTURED_IMAGE_PATH"
    }

    private var editor: SharedPreferences.Editor? = null

    init {
        editor = sharedPreferences.edit()
    }

    fun getInt(key: String) : Int {
        return getInt(key, DEFAULT_INTEGER)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        with(editor!!) {
            putInt(key, value)
            apply()
        }
    }

    fun getLong(key: String) : Long {
        return getLong(key, DEFAULT_LONG)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences!!.getLong(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        with(editor!!) {
            putLong(key, value)
            apply()
        }
    }

    fun getFloat(key: String) : Float {
        return getFloat(key, DEFAULT_FLOAT)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return sharedPreferences!!.getFloat(key, defaultValue)
    }

    fun putFloat(key: String, value: Float) {
        with(editor!!) {
            putFloat(key, value)
            apply()
        }
    }

    fun getString(key: String) : String {
        return getString(key, DEFAULT_STRING)
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences!!.getString(key, defaultValue) ?: ""
    }

    fun putString(key: String, value: String) {
        with(editor!!) {
            putString(key, value)
            apply()
        }
    }

    fun geBoolean(key: String) : Boolean {
        return geBoolean(key, DEFAULT_BOOLEAN)
    }

    fun geBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences!!.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        with(editor!!) {
            putBoolean(key, value)
            apply()
        }
    }

    fun removeKey(key: String) {
        editor!!.remove(key)
    }

    protected fun clearAll() {
        with(editor!!) {
            clear()
            apply()
        }
    }

}