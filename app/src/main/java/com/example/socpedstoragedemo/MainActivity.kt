package com.example.socpedstoragedemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.socpedstoragedemo.accessmedialocation.AccessMediaLocationActivity
import com.example.socpedstoragedemo.downlaoddemo.DownloadImageActivity
import com.example.socpedstoragedemo.filedemo.FileActivity
import com.example.socpedstoragedemo.folderdemo.OpenFolderActivity
import com.example.socpedstoragedemo.imagedemo.ImagePickerActivity
import android.widget.Toast
import android.R.attr.data
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import kotlinx.android.synthetic.main.activity_download_image.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.nio.file.Files.size




class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mIntent = intent
        val action = mIntent.action
        val type = mIntent.type

        if (action == Intent.ACTION_SEND && type != null) {
            if (type.startsWith("image/")) {
                val mUri = mIntent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                Toast.makeText(this, "from outside uri $mUri", Toast.LENGTH_SHORT).show()

                var file =  File(mUri?.path);//create path from uri


                Log.i("TAG","File=== $file")

                val bitmap = BitmapFactory.decodeStream(mUri?.let {
                    contentResolver.openInputStream(
                        it
                    )
                })

                ivGetImage.setImageBitmap(bitmap)


            }
        } else {
            Toast.makeText(this, "from outside", Toast.LENGTH_SHORT).show()
        }

    }



    fun imagePickerDemo(view : View){
        startActivity(Intent(this@MainActivity, ImagePickerActivity::class.java))
    }

    fun downloadImageDemo(view : View){

            startActivity(Intent(this@MainActivity, DownloadImageActivity::class.java))
        }

    fun accessMediaLocation(view : View){

        startActivity(Intent(this@MainActivity, AccessMediaLocationActivity::class.java))
    }

     fun fileDemo(view : View){

        startActivity(Intent(this@MainActivity, FileActivity::class.java))
    }

      fun folderDemo(view : View){

            startActivity(Intent(this@MainActivity, OpenFolderActivity::class.java))
        }



}

