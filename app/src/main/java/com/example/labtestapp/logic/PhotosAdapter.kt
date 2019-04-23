package com.example.labtestapp.logic

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labtestapp.PhotoDetailsActivity
import com.example.labtestapp.R
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.squareup.picasso.Picasso
import androidx.core.view.drawToBitmap
import java.io.ByteArrayOutputStream


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
    }

    private fun setPhotoAndTags(holder: ViewHolder, currentEntry:PhotoEntry) {
        holder.imageURL =currentEntry.url
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
        init{
            itemView.setOnClickListener {
                val detailsIntent = Intent(itemView.context,PhotoDetailsActivity::class.java)
                val bitmap: Bitmap = image.drawToBitmap()
                val bs = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs)
                //detailsIntent.putExtra("byteArray", bs.toByteArray())
                detailsIntent.putExtra("url", imageURL)
                detailsIntent.putExtra("name", name.text.toString())
                detailsIntent.putExtra("tags", tags.text.toString())
                itemView.context.startActivity(detailsIntent)
            }
        }
        val name: TextView = itemView.findViewById(R.id.nameTV)
        val tags: TextView = itemView.findViewById(R.id.tagsTV)
        val date: TextView = itemView.findViewById(R.id.dateTV)
        val image: ImageView = itemView.findViewById(R.id.photo)
        var imageURL: String =""
    }
}