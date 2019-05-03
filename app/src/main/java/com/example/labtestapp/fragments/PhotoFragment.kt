package com.example.labtestapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.labtestapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_photo.view.*


class PhotoFragment : Fragment() {

    private var photoURL:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoURL = it.getString(PHOTO_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        Picasso.get().load(photoURL).into(view.photoView)
        return view
    }

    companion object {
        const val PHOTO_URL = "photoURL"
        @JvmStatic

        fun newInstance(photoURL: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(PHOTO_URL, photoURL)
                }
            }
    }
}
