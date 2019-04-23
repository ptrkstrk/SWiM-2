package com.example.labtestapp.fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.drawable.Drawable
import com.example.labtestapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_photo.*


class PhotoFragment : Fragment() {

    private var photoURL:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoURL = it.getString(PHOTO_URL)
        }
    }

    override fun onStart() {
        super.onStart()
        Picasso.get()
            .load(photoURL)
            .into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    photoView.setImageBitmap(bitmap)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    companion object {
        private val PHOTO_URL = "photoURL"
        @JvmStatic

        fun newInstance(photoURL: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(PHOTO_URL, photoURL)
                }
            }
    }
}
