package com.github.ttaf.gpsphotogallery.util

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class PermissionUtils {
    companion object {
        const val REQUEST_CODE = 200
    }
}

fun Activity.withPermission(
        permission: String,
        requestCode: Int = PermissionUtils.REQUEST_CODE,
        explainAction: (() -> Unit)? = null,
        action: () -> Unit) {
    val permissionGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    if (permissionGranted) {
        action()
    } else {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            explainAction?.invoke()
        }
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }
}