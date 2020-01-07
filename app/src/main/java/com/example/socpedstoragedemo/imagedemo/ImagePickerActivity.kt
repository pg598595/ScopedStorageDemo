package com.example.socpedstoragedemo.imagedemo

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.socpedstoragedemo.R
import com.example.socpedstoragedemo.const.CheckPemission.checkPermissionForReadWrite
import com.example.socpedstoragedemo.const.CheckPemission.requestPermissionForReadWrite
import com.example.socpedstoragedemo.const.Constants.DELETE_PERMISSION_REQUEST
import com.example.socpedstoragedemo.const.Constants.PERMISSION_READ_EXTERNAL_STORAGE
import com.example.socpedstoragedemo.databinding.ActivityImagePickerBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ImagePickerActivity : AppCompatActivity() {


    private val viewModel: ImagePickerViewModel by viewModels()
    private lateinit var binding: ActivityImagePickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_picker)

        val galleryAdapter = ImageViewAdapter { image ->
            deleteImage(image)
        }

        binding.gallery.also { view ->
            view.layoutManager = GridLayoutManager(this, 3)
            view.adapter = galleryAdapter
        }

        viewModel.images.observe(this, Observer<List<MediaStoreModel>> { images ->
            galleryAdapter.submitList(images)
        })

        viewModel.permissionNeededForDelete.observe(this, Observer { intentSender ->
            intentSender?.let {

                startIntentSenderForResult(
                    intentSender,
                    DELETE_PERMISSION_REQUEST,
                    null,
                    0,
                    0,
                    0,
                    null
                )
            }
        })

        binding.openAlbum.setOnClickListener { openMediaStore() }
        binding.grantPermissionButton.setOnClickListener { openMediaStore() }

        if (!checkPermissionForReadWrite(this)) {
            binding.welcomeView.visibility = View.VISIBLE
        } else {
            showImages()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == DELETE_PERMISSION_REQUEST) {
            viewModel.deletePendingImage()
        }
    }

    private fun showImages() {
        viewModel.loadImages()
        binding.welcomeView.visibility = View.GONE
        binding.permissionRationaleView.visibility = View.GONE
    }

    private fun openMediaStore() {
        if (checkPermissionForReadWrite(this)) {
            showImages()
        } else {
            requestPermissionForReadWrite(this)
        }
    }


    private fun deleteImage(image: MediaStoreModel) {

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_dialog_title)
            .setMessage(getString(R.string.delete_dialog_message, image.displayName))
            .setPositiveButton(R.string.delete_dialog_positive) { _: DialogInterface, _: Int ->
                viewModel.deleteImage(image)
            }
            .setNegativeButton(R.string.delete_dialog_negative) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show()
    }



}

