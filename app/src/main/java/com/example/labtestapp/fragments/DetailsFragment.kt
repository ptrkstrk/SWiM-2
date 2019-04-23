package com.example.labtestapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.labtestapp.R
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment() {

    private var photoTags: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoTags = it.getString(PHOTO_TAGS)
        }
    }

    override fun onStart() {
        super.onStart()
        tagsView.text = photoTags
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }


    companion object {
        private val PHOTO_TAGS = "photoTags"
        @JvmStatic
        fun newInstance(photoTags: String) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    //putString(PHOTO_NAME, photoName)
                    putString(PHOTO_TAGS, photoTags)
                }
            }
    }
}
