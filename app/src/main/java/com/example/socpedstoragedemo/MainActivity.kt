package com.example.socpedstoragedemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.socpedstoragedemo.accessmedialocation.AccessMediaLocationActivity
import com.example.socpedstoragedemo.downlaoddemo.DownloadImageActivity
import com.example.socpedstoragedemo.filedemo.FileActivity
import com.example.socpedstoragedemo.folderdemo.OpenFolderActivity


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

