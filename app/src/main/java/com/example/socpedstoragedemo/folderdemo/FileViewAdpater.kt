package com.example.socpedstoragedemo.folderdemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import com.example.socpedstoragedemo.R
import kotlinx.android.synthetic.main.item_file.view.*

class FileViewAdpater(private val c: Context, private val files: ArrayList<DocumentFile>) :
    RecyclerView.Adapter<FileViewAdpater.ViewHolder>() {



    override fun getItemCount(): Int {
        return files.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(c).inflate(R.layout.item_file, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]

        //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

        holder.tvFileName.text = file.name

        holder.tvFileType.text = file.type
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFileName = view.tvFileName
        val tvFileType = view.tvFileType
    }
}