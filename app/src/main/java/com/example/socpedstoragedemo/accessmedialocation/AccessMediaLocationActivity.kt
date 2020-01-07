package com.example.socpedstoragedemo.accessmedialocation

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.example.socpedstoragedemo.R
import com.example.socpedstoragedemo.const.CheckPemission.checkPermissionForAML
import com.example.socpedstoragedemo.const.CheckPemission.requestPermissionForAML
import com.example.socpedstoragedemo.const.Constants.CHOOSE_FILE
import kotlinx.android.synthetic.main.activity_access_media_location.*
import java.io.IOException
import java.io.InputStream


class AccessMediaLocationActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access_media_location)
    }

    fun chooseFile(view: View) {
        Log.i("Tag", "chooseFile")

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {

            openIntentChooser()

        } else {

            if (checkPermissionForAML(this)) {

                openIntentChooser()
            } else {
                Log.i("Tag", "else chooseFile")

                requestPermissionForAML(this)
            }


        }


    }

    fun openIntentChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSE_FILE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE) {
                if (data != null) {


                    var inputStream: InputStream? = null
                    try {
                        inputStream = contentResolver.openInputStream(data.data!!)
                        val exifInterface = ExifInterface(inputStream!!)




                        Log.i(
                            "TAG",
                            "===${exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)}"
                        )
                        Glide.with(ivImage)
                            .load(data.dataString)
                            .thumbnail(0.33f)
                            .into(ivImage)

                        tvImagePath.text =
                            String.format(getString(R.string.path_for_image), data.data)
                        tvLocationLat.text = String.format(
                            getString(R.string.lat_for_image),
                            exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                        )
                        tvLocationLag.text = String.format(
                            getString(R.string.lat_for_image),
                            exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                        )
                        // Now you can extract any Exif tag you want
                        // Assuming the image is a JPEG or supported raw format
                    } catch (e: IOException) {
                        // Handle any errors
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close()
                            } catch (ignored: IOException) {
                            }

                        }
                    }

                }
            }
        }

    }

//    fun ReadExif(file: String): String {
//        var exif = "Exif: $file"
//        try {
//            val exifInterface = ExifInterface(file)
//
//            exif += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)!!
//            exif += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)!!
//            exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME)!!
//            exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE)!!
//            exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL)!!
//            exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)!!
//            exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE)!!
//            exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)!!
//            exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH)!!
//            exif += "\nGPS related:"
//            exif += "\n TAG_GPS_DATESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)!!
//            exif += "\n TAG_GPS_TIMESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP)!!
//            exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)!!
//            exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)!!
//            exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)!!
//            exif += "\n TAG_GPS_LONGITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)!!
//            exif += "\n TAG_GPS_PROCESSING_METHOD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD)!!
//
//            Toast.makeText(
//                this@AccessMediaLocationActivity,
//                "finished",
//                Toast.LENGTH_LONG
//            ).show()
//
//        } catch (e: IOException) {
//
//            e.printStackTrace()
//            Toast.makeText(
//                this@AccessMediaLocationActivity,
//                e.toString(),
//                Toast.LENGTH_LONG
//            ).show()
//        }
//
//        return exif
//    }


}