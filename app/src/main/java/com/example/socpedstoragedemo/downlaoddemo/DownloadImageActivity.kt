package com.example.socpedstoragedemo.downlaoddemo

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.socpedstoragedemo.R
import com.example.socpedstoragedemo.const.CheckPemission.checkPermissionForReadWrite
import com.example.socpedstoragedemo.const.CheckPemission.requestPermissionForReadWrite
import kotlinx.android.synthetic.main.activity_download_image.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DownloadImageActivity : AppCompatActivity() {

    var imgUrl = "https://matrixmarketing.co.za/wp-content/uploads/2017/03/Demo-Page.jpg"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_image)


        Glide.with(this)
            .load(imgUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .dontAnimate()
            .into(ivImageDownload)

    }

    fun downloadImage(view: View) {


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {


            if (checkPermissionForReadWrite(this)) {
                downloadFile(imgUrl)
            } else {
                requestPermissionForReadWrite(this)
            }


        } else {
            downloadFile(imgUrl)
        }

    }

    fun saveFileToLocalFolder(view: View) {
        if (checkPermissionForReadWrite(this)) {
            try {


                val bitmap: Bitmap = (ivImageDownload.drawable as BitmapDrawable).bitmap

                val filepath = saveToInternalStorage(bitmap)





                Toast.makeText(
                    applicationContext,
                    "Download successfully to $filepath",
                    Toast.LENGTH_LONG
                ).show()


            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    "$e=== EACCES (Permission denied)",
                    Toast.LENGTH_LONG
                ).show()
                Log.i("TAG", e.toString())
                e.printStackTrace()

            }


        } else {
            requestPermissionForReadWrite(this)
        }

    }


    private fun downloadFile(uRl: String) {


        val mgr = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(uRl)
        val request = DownloadManager.Request(
            downloadUri
        )

        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
        )
            .setAllowedOverRoaming(false).setTitle("Demo")
            .setDescription("Something useful. No, really.")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "DemoImage.jpg")

        Toast.makeText(
            applicationContext,
            "Download successfully to ${downloadUri.path}",
            Toast.LENGTH_LONG
        ).show()

        mgr.enqueue(request)

    }


    private fun saveToInternalStorage(bitmapImage: Bitmap): String {

        var filepath = ""

        try {

            val testFile = File(
                this.getExternalFilesDir(
                    null
                ), "TestFile.png"
            )
            filepath = testFile.absolutePath
            if (!testFile.exists())
                testFile.createNewFile()

            var fos: FileOutputStream? = null

            fos = FileOutputStream(testFile)

            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)

        } catch (e: IOException) {
            filepath = "not done"
            Log.e("TAG", "Failed")
        }

        return filepath

    }

}


//    fun pdf(view: View) {
//
//
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
//
//            try {
//
//                val path = Environment.getExternalStorageDirectory()?.absolutePath  + "/test.txt"
//                val myFile = File(path)
//                myFile.createNewFile()
//                var fOut = FileOutputStream(myFile)
//                var myOutWriter = OutputStreamWriter(fOut)
//                myOutWriter.append(edData.text.toString())
//                myOutWriter.close()
//                fOut.close()
//                Toast.makeText(applicationContext, "saved===${myFile.absolutePath}", Toast.LENGTH_LONG).show()
//                Log.i("TAG","File path is $myFile")
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//            }
//
//        }
//        else{
//
//            try {
//
//                val path = Environment.getExternalStorageDirectory()?.absolutePath  + "/test.txt"
//                val myFile = File(path)
//                myFile.createNewFile()
//                var fOut = FileOutputStream(myFile)
//                var myOutWriter = OutputStreamWriter(fOut)
//                myOutWriter.append(edData.text.toString())
//                myOutWriter.close()
//                fOut.close()
//                Toast.makeText(applicationContext, "saved===${myFile.absolutePath}", Toast.LENGTH_LONG).show()
//                Log.i("TAG","File path is $myFile")
//            } catch (e: FileNotFoundException) {
//                Toast.makeText(applicationContext, "saved===${e}", Toast.LENGTH_LONG).show()
//                e.printStackTrace()
//            }
//
//
//
//
//        }


//
//
//        val values = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, "abc")
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PP/")
//            put(MediaStore.Images.Media.IS_PENDING, 1)
//        }
//
//        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
//        val imageUri = this.contentResolver.insert(collection, values)
//
//        try {
//
//            var stream: OutputStream? = null
//
//
//            stream = FileOutputStream(imageUri?.path)
//
//
//
//            stream.flush()
//
//
//            stream.close()
//
//        } catch (e: IOException) // Catch the exception
//        {
//            e.printStackTrace()
//        }


//
//        var fOut = FileOutputStream(File(imageUri?.path))
//        var myOutWriter = OutputStreamWriter(fOut)
//        myOutWriter.append(edData.text.toString())
//        myOutWriter.close()
//        fOut.close()

//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//
//        startActivityForResult(intent, 123)


// }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == Constants.PERMISSION_READ_EXTERNAL_STORAGE) {
//            if (resultCode == Activity.RESULT_OK) {
//                val directoryUri = data?.data ?: return
//                Log.i("Tag","======$directoryUri")
//            } else {
//                // The user cancelled the request.
//            }
//        }
//    }
//
//


