package com.example.socpedstoragedemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageViewAdapter(val onClick: (MediaStoreModel) -> Unit) :
    ListAdapter<MediaStoreModel, ImageViewHolder>(MediaStoreModel.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val mediaStoreImage = getItem(position)
        holder.rootView.tag = mediaStoreImage

        Glide.with(holder.imageView)
            .load(mediaStoreImage.contentUri)
            .thumbnail(0.33f)
            .centerCrop()
            .into(holder.imageView)
    }

}

class ImageViewHolder(view: View, onClick: (MediaStoreModel) -> Unit) :
    RecyclerView.ViewHolder(view) {
    val rootView = view
    val imageView: ImageView = view.findViewById(R.id.image)

    init {
        imageView.setOnClickListener {
            val image = rootView.tag as? MediaStoreModel ?: return@setOnClickListener
            onClick(image)
        }
    }
}
