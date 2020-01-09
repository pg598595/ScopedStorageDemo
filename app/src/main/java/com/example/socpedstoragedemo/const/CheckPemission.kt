package com.example.socpedstoragedemo.const

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.socpedstoragedemo.const.Constants.PERMISSION_READ_EXTERNAL_STORAGE
import com.example.socpedstoragedemo.const.Constants.PERMISSION_REQUEST_CODE
import java.util.ArrayList


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
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_READ_EXTERNAL_STORAGE
        )

    }

    fun checkMultiplePermissions(permissionList: ArrayList<String>,context: Context): Boolean {
        val arrPermission = checkPermissions(permissionList,context)
        if (arrPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as Activity,
                arrPermission,
                PERMISSION_READ_EXTERNAL_STORAGE)
            return true
        }
        return false
    }

    private fun checkPermissions(permissionList: ArrayList<String>,context: Context): Array<String?> {
        val arrPermission = ArrayList<String>()

        for (i in permissionList.indices) {
            val permission = permissionList[i]
            if (ContextCompat.checkSelfPermission(context,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                arrPermission.add(permission)
            }
        }
        return arrPermission.toTypedArray()
    }


}