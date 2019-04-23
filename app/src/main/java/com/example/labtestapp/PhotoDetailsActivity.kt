package com.example.labtestapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.labtestapp.fragments.DetailsFragment
import com.example.labtestapp.fragments.PhotoFragment

class PhotoDetailsActivity : AppCompatActivity() {

    var isPhotoFragmentLoaded = true
    val fragmentsManager = supportFragmentManager
    var name = ""
    var photoURL = ""
    var tags = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        name = intent.getStringExtra("name")
        photoURL = intent.getStringExtra("url")
        tags = intent.getStringExtra("tags")
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