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
import com.squareup.picasso.Target


class PhotosAdapter(val entriesList: ArrayList<PhotoEntry>) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    //private lateinit var currentEntry:PhotoEntry

    companion object {
       const val VISIBLE_TAGS_AMOUNT = 3
        const val SIMILAR_PHOTOS_MAX_AMOUNT = 6
        const val FAIL_LOG = "Failure"
        const val TAGS_SEPARATOR = " "
        const val HASH = "#"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_main_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIndex = itemCount - (position + 1)
        val currentEntry = entriesList[currentIndex]
        holder.name.text = currentEntry.name
        holder.imageURL = currentEntry.url
        setPhotoAndTags(holder, currentEntry)
        holder.date.text = currentEntry.date
        setOnClickListener(holder, currentEntry)
    }

    private fun setPhotoAndTags(holder: ViewHolder, entry: PhotoEntry) {
        val imgTarget = createImgTarget(holder, entry)
        Picasso.get().load(holder.imageURL).into(imgTarget)
    }

    private fun createImgTarget(holder: ViewHolder, entry: PhotoEntry): Target {
        return object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                val vision = FirebaseVisionImage.fromBitmap(bitmap)
                val labeler = FirebaseVision.getInstance().onDeviceImageLabeler
                labeler.processImage(vision)
                    .addOnSuccessListener { labels ->
                        holder.image.setImageBitmap(bitmap)
                        var tags = labels.map { it.text }
                        entry.tags = ArrayList(tags)
                        tags = tags.map{ HASH + it}
                        holder.tags.text = tags.joinToString(
                            TAGS_SEPARATOR, "", "", VISIBLE_TAGS_AMOUNT, "")
                    }
                    .addOnFailureListener {
                        Log.wtf(FAIL_LOG, it.message)
                    }
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

        }
    }

    private fun setOnClickListener(holder:ViewHolder, entry:PhotoEntry) {
        holder.itemView.setOnClickListener {
            val detailsIntent = prepareDetailsIntent(holder, entry)
            holder.itemView.context.startActivity(detailsIntent)
        }
    }

    private fun prepareDetailsIntent(holder:ViewHolder, entry:PhotoEntry): Intent {
        val detailsIntent = Intent(holder.itemView.context, PhotoDetailsActivity::class.java)
        detailsIntent.putExtra(PhotoDetailsActivity.URL_KEY, holder.imageURL)
        detailsIntent.putExtra(PhotoDetailsActivity.NAME_KEY, holder.name.text.toString())
        detailsIntent.putExtra(PhotoDetailsActivity.TAGS_KEY, holder.tags.text.toString())
        detailsIntent.putExtra(PhotoDetailsActivity.DATE_KEY, holder.date.text.toString())
        val similarPhotosUrls = findSimilarPhotos(entry)
        detailsIntent.putExtra(PhotoDetailsActivity.SIMILAR_PHOTOS_URLS_KEY,similarPhotosUrls)
        return detailsIntent
    }

    override fun getItemCount() = entriesList.size

    fun removeAt(deletedIndex: Int) {
        entriesList.removeAt(itemCount - (deletedIndex + 1))
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
                if(similarity > 0)
                    entriesWithSimilarities.add(Pair(photoEntry, similarity))
            }
        val mostSimilarEntries = extractBestOnes(entriesWithSimilarities)
        return ArrayList(mostSimilarEntries.map{it.first.url})
    }

    private fun extractBestOnes(entriesWithSimilarities: ArrayList<Pair<PhotoEntry, Int>>)
            : List<Pair<PhotoEntry, Int>> {
        val sortedEntries = entriesWithSimilarities.sortedWith(compareBy {it.second})
        val lastIndex = minOf(SIMILAR_PHOTOS_MAX_AMOUNT, sortedEntries.size)
        return sortedEntries.asReversed().subList(0, lastIndex)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.nameTV)
        val tags: TextView = itemView.findViewById(R.id.tagsTV)
        val date: TextView = itemView.findViewById(R.id.dateTV)
        val image: ImageView = itemView.findViewById(R.id.photo)
        var imageURL: String = ""
    }
}