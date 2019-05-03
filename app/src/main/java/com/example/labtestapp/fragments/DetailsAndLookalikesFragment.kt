package com.example.labtestapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.labtestapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.view.*


class DetailsAndLookalikesFragment : Fragment() {

    private var date = ""
    private var name = ""
    private var photoTags = ""
    private var similarUrls : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(PHOTO_NAME)
            date = it.getString(PHOTO_DATE)
            photoTags = it.getString(PHOTO_TAGS)
            similarUrls = it.getStringArrayList(PHOTO_LOOKALIKES)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        performTransaction()
        return inflater.inflate(R.layout.details_fragment_holders_layout, container, false)
    }

    private fun performTransaction() {
        val transaction = childFragmentManager.beginTransaction()
        val detailsFragment = DetailsFragment.newInstance(name, date, photoTags)
        transaction.replace(R.id.top_fragment_holder, detailsFragment)
        val lookalikesFragment = LookalikesFragment.newInstance(similarUrls)
        transaction.replace(R.id.bottom_fragment_holder, lookalikesFragment)
        transaction.commit()
    }


    companion object {
        private const val PHOTO_TAGS = "photoTags"
        private const val PHOTO_NAME = "photoName"
        private const val PHOTO_DATE = "photoDate"
        private const val PHOTO_LOOKALIKES = "photoLookalikes"

        @JvmStatic
        fun newInstance(name: String, date:String, photoTags: String, lookalikes: ArrayList<String>) =
            DetailsAndLookalikesFragment().apply {
                arguments = Bundle().apply {
                    putString(PHOTO_TAGS, photoTags)
                    putString(PHOTO_NAME, name)
                    putString(PHOTO_DATE, date)
                    putStringArrayList(PHOTO_LOOKALIKES, lookalikes)
                }
            }
    }
}

class DetailsFragment : Fragment(){
    private var name = ""
    private var date = ""
    private var tags = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(PHOTO_NAME)
            date = it.getString(PHOTO_DATE)
            tags = it.getString(PHOTO_TAGS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_details, container, false)
        v.tagsView.text = tags
        v.nameView.text = name
        v.dateView.text = date
        return v
    }


    companion object {
        private const val PHOTO_TAGS = "photoTags"
        private const val PHOTO_NAME = "photoName"
        private const val PHOTO_DATE = "photoDate"

        @JvmStatic
        fun newInstance(name: String, date:String, photoTags: String) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(PHOTO_TAGS, photoTags)
                    putString(PHOTO_NAME, name)
                    putString(PHOTO_DATE, date)
                }
            }
    }

}

class LookalikesFragment : Fragment(){

    private lateinit var lookalikes : ArrayList<String>
    private val imageViews : ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareImageViews()
        arguments?.let {
            lookalikes = it.getStringArrayList(PHOTO_LOOKALIKES)
        }
    }

    override fun onStart() {
        super.onStart()
        prepareImageViews()
    }

    private fun prepareImageViews() {
        imageViews.add(R.id.imageView1)
        imageViews.add(R.id.imageView2)
        imageViews.add(R.id.imageView3)
        imageViews.add(R.id.imageView4)
        imageViews.add(R.id.imageView5)
        imageViews.add(R.id.imageView6)
    }

    private fun loadImages(view:View) {
        var currImageV: ImageView
        for((index, imageViewID) in imageViews.withIndex()) {
            if(index < lookalikes.size){
                currImageV = view.findViewById(imageViewID)
                Picasso.get().load(lookalikes[index]).into(currImageV)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_similar_photos, container, false)
        loadImages(view)
        return view
    }

    companion object {
        private const val PHOTO_LOOKALIKES = "photoLookalikes"

        @JvmStatic
        fun newInstance(lookalikes: ArrayList<String>) =
            LookalikesFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(PHOTO_LOOKALIKES, lookalikes)
                }
            }
    }

}
