package com.example.socpedstoragedemo.folderdemo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.socpedstoragedemo.R
import com.example.socpedstoragedemo.const.Constants.OPEN_DIRECTORY_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_open_folder.*

class OpenFolderActivity : AppCompatActivity() {

    var fileList = ArrayList<DocumentFile>()
    val adpater = FileViewAdpater(this, fileList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_folder)


        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        rvTree.layoutManager = layoutManager


    }

    fun openDocTree(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            fileList.clear()
            val directoryUri = data?.data ?: return

            contentResolver.takePersistableUriPermission(
                directoryUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            loadDirectory(directoryUri)


        }
    }

    fun loadDirectory(directoryUri: Uri) {
        val documentsTree = DocumentFile.fromTreeUri(application, directoryUri) ?: return
        val childDocuments = documentsTree.listFiles().asList()


        for (i in 0 until childDocuments.size) {
            Log.i("TAG", "${childDocuments[i].type}")
            fileList.add(childDocuments[i])

        }
        rvTree.adapter = adpater

    }

}