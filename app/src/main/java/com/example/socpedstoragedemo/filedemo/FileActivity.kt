package com.example.socpedstoragedemo.filedemo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socpedstoragedemo.R
import com.example.socpedstoragedemo.const.Constants.CREATE_REQUEST_CODE
import com.example.socpedstoragedemo.const.Constants.OPEN_DOCUMENT_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_files.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class FileActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)
    }


    fun newFile(view: View) {


        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TITLE, "${edFileName.text.trim()}.txt")

        startActivityForResult(intent, CREATE_REQUEST_CODE)


    }


    fun openPdfFile(view: View) {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {

            type = "application/pdf"


            addCategory(Intent.CATEGORY_OPENABLE)


            flags = flags or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST_CODE)


    }


    private fun writeFileContent(uri: Uri?) {
        try {
            val pfd = uri?.let { this.contentResolver.openFileDescriptor(it, "w") }

            val fileOutputStream = FileOutputStream(
                pfd!!.fileDescriptor
            )

            val textContent = edText.text.toString()

            fileOutputStream.write(textContent.toByteArray())

            fileOutputStream.close()
            pfd.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    @SuppressLint("NewApi")
    fun downalodPDf(view: View) {
        // create a new document
        val document = PdfDocument()
        // crate a page description
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        // start a page
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.setColor(Color.RED)
        canvas.drawCircle(50F, 50F, 30F, paint)
        paint.setColor(Color.BLACK)
        canvas.drawText("aaabbbccc", 80F, 50F, paint)
        // finish the page
        document.finishPage(page)

        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, "demopdf.pdf")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val resolver = contentResolver
        // Log.i("TAG","${MediaStore.getExternalVolumeNames(this)}")

        val volumeNames = MediaStore.getExternalVolumeNames(this)


        val collection = MediaStore.Downloads.getContentUri(volumeNames.elementAt(1))

        val item = resolver.insert(collection, values)


        if (item != null) {
            resolver.openOutputStream(item).use { out ->
                document.writeTo(out);
            }
        }
        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        item?.let { resolver.update(it, values, null, null) }

        Toast.makeText(
            applicationContext,
            "Download successfully to ${item?.path}",
            Toast.LENGTH_LONG
        ).show()


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var currentUri: Uri? = null


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CREATE_REQUEST_CODE) {
                if (data != null) {

                    currentUri = data.data
                    writeFileContent(currentUri)


                }

            }

        }
        if (requestCode == OPEN_DOCUMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { documentUri ->

                contentResolver.takePersistableUriPermission(
                    documentUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                tvPdfName.text = documentUri.path.toString()
            }
        }
    }
}

