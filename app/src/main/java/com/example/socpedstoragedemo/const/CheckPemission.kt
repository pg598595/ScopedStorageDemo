package com.example.socpedstoragedemo.const

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.socpedstoragedemo.const.Constants.PERMISSION_READ_EXTERNAL_STORAGE
import com.example.socpedstoragedemo.const.Constants.PERMISSION_REQUEST_CODE


object CheckPemission {




    //Check if Permission granted

    fun checkPermissionForAML(context: Context): Boolean {
        Log.i("Tag", "checkPermissionForAML")
        val result: Int =
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_MEDIA_LOCATION
            )
        return result == PackageManager.PERMISSION_GRANTED
    }

    //Request Permission if not given

    fun requestPermissionForAML(context: Context) {
        Log.i("Tag", "requestPermissionForAML")

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(android.Manifest.permission.ACCESS_MEDIA_LOCATION), PERMISSION_REQUEST_CODE
        )

    }

    fun checkPermissionForReadWrite(context: Context): Boolean {
        Log.i("Tag", "checkPermissionForREadWriteBelowP")
        val result: Int =
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        return result == PackageManager.PERMISSION_GRANTED
    }

    //Request Permission if not given

    fun requestPermissionForReadWrite(context: Context) {
        Log.i("Tag", "requestPermissionForREadWriteBelowP")

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_READ_EXTERNAL_STORAGE
        )

    }




}