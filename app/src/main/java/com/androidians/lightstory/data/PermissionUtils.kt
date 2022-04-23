package com.androidians.lightstory.data

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PermissionUtils(private val context: AppCompatActivity) {

    private val cameraPermissionLauncher = context.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {

        } else {

        }
    }

}