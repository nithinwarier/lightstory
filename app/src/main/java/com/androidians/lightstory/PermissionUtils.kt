package com.androidians.lightstory

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object PermissionUtils {

    private fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean {
        var flag = false
        for (permission in permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                    flag = true
                } else {
                    return false
                }
            }
        }
        return flag
    }

}