package com.example.socpedstoragedemo

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class EditImageActivity : AppCompatActivity() {


    private val CHOOSE_FILE = 2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image)
    }

    fun editImage(view: View){

        val intent = Intent()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.action = Intent.ACTION_GET_CONTENT
        } else {
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            intent.addCategory(Intent.CATEGORY_OPENABLE)
        }


            intent.type = "image/*"
            //intent.addCategory(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        Environment.getExternalStorageDirectory()

            startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSE_FILE)




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var currentUri: Uri? = null


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE) {
                if (data != null) {


                    currentUri = data.data


                    currentUri?.let { editImage(it) }



                }
            }
        }

    }



    fun editImage(uri : Uri){


        try{

            contentResolver.openFileDescriptor(uri,"w")?.use {


                Log.i("TAG","writing in files accessed")


            }




        }catch (securityException : SecurityException){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

                val recoverableSecurityException = securityException as? RecoverableSecurityException ?: throw securityException


                val intentSender = recoverableSecurityException.userAction.actionIntent.intentSender

                intentSender?.let {
                    startIntentSenderForResult(intentSender,4,null,0,0,0,null)
                }


            }
            else{
                throw securityException
            }




        }




    }





}