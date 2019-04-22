package com.example.labtestapp.logic

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labtestapp.R
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.squareup.picasso.Picasso

class PhotosAdapter(private val entriesList: ArrayList<PhotoEntry>) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_main_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIndex = itemCount - (position + 1)
        val currentEntry = entriesList[currentIndex]
        holder.name.text = currentEntry.name
        setPhotoAndTags(holder, currentEntry)
        holder.date.text = currentEntry.date

        holder.itemView.setOnClickListener{
            holder.name.text = ""
        }
    }

    private fun setPhotoAndTags(holder: ViewHolder, currentEntry:PhotoEntry) {
        Picasso.get()
            .load(currentEntry.url)
            .into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    val vision = FirebaseVisionImage.fromBitmap(bitmap)
                    val labeler = FirebaseVision.getInstance().onDeviceImageLabeler
                    labeler.processImage(vision)
                        .addOnSuccessListener {
                            holder.image.setImageBitmap(bitmap)
                            holder.tags.text = it.joinToString(
                                " ", "", "", 3, ""){ it.text }
                        }
                        .addOnFailureListener {
                            Log.wtf("Failure", it.message)
                        }
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            })
    }

    override fun getItemCount() = entriesList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameTV)
        val tags: TextView = itemView.findViewById(R.id.tagsTV)
        val date: TextView = itemView.findViewById(R.id.dateTV)
        val image: ImageView = itemView.findViewById(R.id.photo)
    }
}