package com.moali.compass

import App
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shared.utils.permission.AndroidPermissionHandler

class MainActivity : ComponentActivity() {
    private lateinit var permissionHandler: AndroidPermissionHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionHandler = AndroidPermissionHandler(this)
        if (!permissionHandler.isLocationPermissionGranted()) {
            permissionHandler.requestLocationPermission()
        }
        setContent {
            App()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AndroidPermissionHandler.LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, proceed with fetching location
            } else {
                // Permission denied, show a message to the user
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}