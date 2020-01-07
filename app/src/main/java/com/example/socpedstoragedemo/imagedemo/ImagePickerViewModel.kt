package com.example.socpedstoragedemo.imagedemo

import android.annotation.SuppressLint
import android.app.Application
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentUris
import android.content.IntentSender
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ImagePickerViewModel(application: Application) : AndroidViewModel(application) {
    private val _images = MutableLiveData<List<MediaStoreModel>>()
    val images: LiveData<List<MediaStoreModel>> get() = _images

    private var contentObserver: ContentObserver? = null

    private var pendingDeleteImage: MediaStoreModel? = null
    private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
    val permissionNeededForDelete: LiveData<IntentSender?> = _permissionNeededForDelete

    fun loadImages() {
        viewModelScope.launch {
            val imageList = queryImages()
            _images.postValue(imageList)

            if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerObserver(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                ) {
                    loadImages()
                }
            }
        }
    }

    fun deleteImage(image: MediaStoreModel) {
        viewModelScope.launch {
            performDeleteImage(image)
        }
    }

    fun deletePendingImage() {
        pendingDeleteImage?.let { image ->
            pendingDeleteImage = null
            deleteImage(image)
        }
    }

    private suspend fun queryImages(): List<MediaStoreModel> {
        val images = mutableListOf<MediaStoreModel>()

        withContext(Dispatchers.IO) {

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
            )

            val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"


            val selectionArgs = arrayOf(
                // Release day of the G1. :)
                dateToTimestamp(day = 22, month = 10, year = 2008).toString()
            )


            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

            getApplication<Application>().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateTakenColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

                while (cursor.moveToNext()) {

                    // Here we'll use the column indexs that we found above.
                    val id = cursor.getLong(idColumn)
                    val dateTaken = Date(cursor.getLong(dateTakenColumn))
                    val displayName = cursor.getString(displayNameColumn)

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    val image = MediaStoreModel(id, displayName, dateTaken, contentUri)
                    images += image


                    Log.v("TAG", image.toString())
                }
            }
        }

        Log.v("TAG", "Found ${images.size} images")
        return images
    }

    private suspend fun performDeleteImage(image: MediaStoreModel) {
        withContext(Dispatchers.IO) {
            try {

                getApplication<Application>().contentResolver.delete(
                    image.contentUri,
                    "${MediaStore.Images.Media._ID} = ?",
                    arrayOf(image.id.toString())
                )
            } catch (securityException: SecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val recoverableSecurityException =
                        securityException as? RecoverableSecurityException
                            ?: throw securityException

                    // Signal to the Activity that it needs to request permission and
                    // try the delete again if it succeeds.

                    pendingDeleteImage = image
                    _permissionNeededForDelete.postValue(
                        recoverableSecurityException.userAction.actionIntent.intentSender
                    )
                } else {
                    throw securityException
                }
            }
        }
    }


    @Suppress("SameParameterValue")
    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            formatter.parse("$day.$month.$year")?.time ?: 0
        }


    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }
}


private fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}


