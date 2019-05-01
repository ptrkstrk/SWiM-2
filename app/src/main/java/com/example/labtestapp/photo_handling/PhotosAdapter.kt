package com.example.labtestapp.photo_handling

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



class PhotosAdapter(val entriesList: ArrayList<PhotoEntry>) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    companion object {
       const val VISIBLE_TAGS_AMOUNT = 3
        const val SIMILAR_PHOTOS_AMOUNT = 6
        const val FAIL_LOG = "Failure"
        const val TAGS_SEPARATOR = " "
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_main_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIndex = itemCount - (position + 1)
        val currentEntry = entriesList[currentIndex]
        holder.name.text = currentEntry.name
        holder.imageURL = currentEntry.url
        setPhotoAndTags(holder, currentEntry)
        holder.date.text = currentEntry.date
        val similarPhotosUrls = findSimilarPhotos(currentEntry)
        holder.setOnClickListener(similarPhotosUrls)
    }

    private fun setPhotoAndTags(holder: ViewHolder, entry: PhotoEntry ) {
        Picasso.get()
            .load(holder.imageURL)
            .into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    val vision = FirebaseVisionImage.fromBitmap(bitmap)
                    val labeler = FirebaseVision.getInstance().onDeviceImageLabeler
                    labeler.processImage(vision)
                        .addOnSuccessListener { labels ->
                            holder.image.setImageBitmap(bitmap)
                            val tags = labels.map { it.text }
                            entry.tags = ArrayList(tags)
                            holder.tags.text = tags.joinToString(
                                TAGS_SEPARATOR, "", "", VISIBLE_TAGS_AMOUNT, "")
                        }
                        .addOnFailureListener {
                            Log.wtf(FAIL_LOG, it.message)
                        }
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            })
    }

    override fun getItemCount() = entriesList.size

    fun removeAt(deletedIndex: Int) {
        entriesList.removeAt(itemCount - (deletedIndex + 1)) // NW CZY INDEKS NIE JEST ZLE!!!!
        notifyItemRemoved(deletedIndex)
    }

    fun add(entry: PhotoEntry) {
        entriesList.add(entry)
        notifyItemInserted(0)
    }

    private fun findSimilarPhotos(examinedEntry: PhotoEntry): ArrayList<String> {
        val entriesWithSimilarities:ArrayList<Pair<PhotoEntry, Int>> = ArrayList()
        var similarity: Int
        for(photoEntry in entriesList)
            if(photoEntry != examinedEntry) {
                similarity = examinedEntry.evaluateSimilarity(photoEntry)
                entriesWithSimilarities.add(Pair(photoEntry, similarity))
            }
        val sortedEntries = entriesWithSimilarities.sortedWith(compareBy {it.second} )
        val lastIndex = sortedEntries.size - 1
        val mostSimilarEntries = sortedEntries.subList(lastIndex - SIMILAR_PHOTOS_AMOUNT, lastIndex)
        return ArrayList(mostSimilarEntries.map{it.first.url})
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setOnClickListener(similarPhotosUrls: ArrayList<String>) {
            itemView.setOnClickListener {
                val detailsIntent = Intent(itemView.context, PhotoDetailsActivity::class.java)
                detailsIntent.putExtra(PhotoDetailsActivity.URL_KEY, imageURL)
                detailsIntent.putExtra(PhotoDetailsActivity.NAME_KEY, name.text.toString())
                detailsIntent.putExtra(PhotoDetailsActivity.TAGS_KEY, tags.text.toString())
                detailsIntent.putExtra(PhotoDetailsActivity.DATE_KEY, date.text.toString())
                detailsIntent.putExtra(PhotoDetailsActivity.SIMILAR_PHOTOS_URLS_KEY, similarPhotosUrls)
                itemView.context.startActivity(detailsIntent)
            }
        }

        val name: TextView = itemView.findViewById(R.id.nameTV)
        val tags: TextView = itemView.findViewById(R.id.tagsTV)
        val date: TextView = itemView.findViewById(R.id.dateTV)
        val image: ImageView = itemView.findViewById(R.id.photo)
        var imageURL: String = ""
    }
}