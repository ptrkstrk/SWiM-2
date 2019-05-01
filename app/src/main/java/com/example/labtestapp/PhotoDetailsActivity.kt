package com.example.labtestapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.labtestapp.fragments.DetailsFragment
import com.example.labtestapp.fragments.PhotoFragment

class PhotoDetailsActivity : AppCompatActivity() {

    companion object {
        const val URL_KEY = "url"
        const val NAME_KEY = "name"
        const val TAGS_KEY = "tags"
        const val SIMILAR_PHOTOS_URLS_KEY = "similar photos"
        const val DATE_KEY = "date"
    }

    var isPhotoFragmentLoaded = true
    val fragmentsManager = supportFragmentManager
    var name = ""
    var photoURL = ""
    var tags = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        name = intent.getStringExtra(NAME_KEY)
        photoURL = intent.getStringExtra(URL_KEY)
        tags = intent.getStringExtra(TAGS_KEY)
        showPhotoFragment()

        val toggleButton = findViewById<Button>(R.id.toggleBtn)
        toggleButton.setOnClickListener{
            if (isPhotoFragmentLoaded)
                showDetailsFragment()
            else
                showPhotoFragment()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showPhotoFragment(){
        val fragment = PhotoFragment.newInstance(photoURL)
        replaceFragment(fragment)
        isPhotoFragmentLoaded = true
    }
    private fun showDetailsFragment(){
        val fragment = DetailsFragment.newInstance(tags)
        replaceFragment(fragment)
        isPhotoFragmentLoaded = false
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = fragmentsManager.beginTransaction()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}