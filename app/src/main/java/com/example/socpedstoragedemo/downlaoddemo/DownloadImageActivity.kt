package com.example.socpedstoragedemo.downlaoddemo

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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

    var imgUrl = "https://cdn.pixabay.com/photo/2014/04/14/20/11/japanese-cherry-trees-324175__340.jpg"




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


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {


            if (checkPermissionForReadWrite(this)) {
                downloadFile(imgUrl)
            } else {
                requestPermissionForReadWrite(this)
            }


        } else {
            downloadFile(imgUrl)
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


    fun saveFileToLocalFolder(view: View) {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (checkPermissionForReadWrite(this)) {
                downloadtoLocalFolder()
            } else {
                requestPermissionForReadWrite(this)
            }

        }
        else{
            downloadtoLocalFolder()

        }

    }

    private fun downloadtoLocalFolder(){
        try {

            var bitmap = (ivImageDownload.drawable as BitmapDrawable).bitmap

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
    }




    private fun saveToInternalStorage(bitmapImage: Bitmap?): String {

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

            bitmapImage?.compress(Bitmap.CompressFormat.PNG, 100, fos)

        } catch (e: IOException) {
            filepath = "not done"
            Log.e("TAG", "Failed")
        }

        return filepath

    }

    fun downloadImageWithMediaStore(view: View){

        try{

            var bitmap = (ivImageDownload.drawable as BitmapDrawable).bitmap

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

//                Toast.makeText(
//                        applicationContext,
//                        "support in ANDROID 10",
//                        Toast.LENGTH_LONG
//                    ).show()


                val file =  File(getExternalFilesDir(null), "DemoFile.jpg")

                try {

                    var fos: FileOutputStream? = null

                    fos = FileOutputStream(file)

                    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)

                    Toast.makeText(
                        applicationContext,
                        "Download successfully to  ${file.path}",
                        Toast.LENGTH_LONG
                    ).show()

                } catch ( e:IOException) {
                    // Unable to create file, likely because external storage is
                    // not currently mounted.
                    Log.i("ExternalStorage", "Error writing $file", e)
                    Toast.makeText(
                        applicationContext,
                        "Error writing ${file.path}",
                        Toast.LENGTH_LONG
                    ).show()
                }



            }
            else{


                val values = ContentValues().apply{
                    put(MediaStore.Images.Media.DISPLAY_NAME,"imgMS.jpeg")
                    put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
                    put(MediaStore.Images.Media.IS_PENDING,1)	}

                val resolver = contentResolver


                val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)


                val item = resolver.insert(collection,values)


                if (item != null) {
                    resolver.openOutputStream(item).use { out ->
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    }
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING,0)
                    resolver.update(item,values,null,null)

                    Toast.makeText(
                        applicationContext,
                        "Download successfully to ${item.path}",
                        Toast.LENGTH_LONG
                    ).show()


                }


            }

        }
        catch (e:Exception){
            Log.i("Tag","$e")
            Toast.makeText(
                applicationContext,
                "$e",
                Toast.LENGTH_LONG
            ).show()

        }

    }


    fun downloadImageWithMediaStoreToSdCard(view: View){

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){


            try {
                var bitmap = (ivImageDownload.drawable as BitmapDrawable).bitmap

                    var path = Environment.getExternalStorageDirectory().toString()

                    var filename =  File("$path/image.jpg")

                    var out =  FileOutputStream(filename)

                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    out.flush()
                    out.close()

                    MediaStore.Images.Media.insertImage(contentResolver, filename.absolutePath, filename.name, filename.name)

                    Toast.makeText(applicationContext, "Saved in  $filename", Toast.LENGTH_LONG).show()
                } catch ( e:Exception) {
                    e.printStackTrace()
                }


        }
        else{
            try{
                var bitmap = (ivImageDownload.drawable as BitmapDrawable).bitmap
                val values = ContentValues().apply{
                    put(MediaStore.Images.Media.DISPLAY_NAME,"imgMS.jpeg")
                    put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
                    put(MediaStore.Images.Media.IS_PENDING,1)	}

                val resolver = contentResolver
                // Log.i("TAG","${MediaStore.getExternalVolumeNames(this)}")

                val volumeNames = MediaStore.getExternalVolumeNames(this)


                val collection = MediaStore.Images.Media.getContentUri(volumeNames.elementAt(1))


                val item = resolver.insert(collection,values)


                if (item != null) {
                    resolver.openOutputStream(item).use { out ->
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    }
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING,0)
                    resolver.update(item,values,null,null)

                    Toast.makeText(
                        applicationContext,
                        "Download successfully to ${item.path}",
                        Toast.LENGTH_LONG
                    ).show()


                }

            }
            catch (e:Exception){
                Toast.makeText(
                    applicationContext,
                    "$e",
                    Toast.LENGTH_LONG
                ).show()

            }

        }


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


