package com.example.labtestapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.labtestapp.AddPhotoActivity.Companion.PHOTO_DATE_KEY
import com.example.labtestapp.AddPhotoActivity.Companion.PHOTO_NAME_KEY
import com.example.labtestapp.AddPhotoActivity.Companion.PHOTO_URL_KEY
import com.example.labtestapp.logic.PhotoEntry
import com.example.labtestapp.logic.PhotosAdapter

import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.labtestapp.logic.RecyclerRowTouchHelper


class MainActivity : AppCompatActivity(), RecyclerRowTouchHelper.RecyclerItemTouchHelperListener {

    companion object {
        const val PHOTOS_SP: String = "photosSharedPreferences"
        const val PHOTOS_SP_KEY: String = "photos"
        const val ADD_PHOTO:Int = 997
    }

    private var entriesList: ArrayList<PhotoEntry> = ArrayList()
    private var adapter:PhotosAdapter ?= null
    private var sharedPrefsUpToDate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setUpLayout()
    }

    override fun onPause() {
        super.onPause()
        if(!sharedPrefsUpToDate)
            updateSP()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val deletedIndex = viewHolder.adapterPosition
        entriesList.removeAt(entriesList.size - (deletedIndex + 1))
        adapter?.notifyItemRemoved(deletedIndex)
        sharedPrefsUpToDate = false
        //updateSP()
    }

    private fun attachTouchHelper() {
        val itemTouchHelperCallback = RecyclerRowTouchHelper(this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mainRV)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun setUpLayout() {
        val photosSP = getSharedPreferences(PHOTOS_SP, Context.MODE_PRIVATE)
        if(photosSP.contains(PHOTOS_SP_KEY)){
            val type = object : TypeToken<ArrayList<PhotoEntry>>() {}.type
            entriesList = Gson().fromJson<ArrayList<PhotoEntry>>(photosSP.getString(PHOTOS_SP_KEY, ""), type)
        }
        mainRV.layoutManager = LinearLayoutManager(this)
        attachAdapter()
        attachTouchHelper()
    }

    private fun attachAdapter() {
        adapter = PhotosAdapter(entriesList)
        mainRV.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_button ->{
                val addPhotoIntent = Intent(this, AddPhotoActivity::class.java)
                startActivityForResult(addPhotoIntent, ADD_PHOTO)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                ADD_PHOTO -> {
                    val entry = prepareEntry(data)
                    saveEntry(entry)
                }
            }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun prepareEntry(data: Intent?): PhotoEntry {
        val name = data?.extras!!.getString(PHOTO_NAME_KEY)
        val url = data.extras!!.getString(PHOTO_URL_KEY)
        val date = data.extras!!.getString(PHOTO_DATE_KEY)

        return PhotoEntry(url, name, date)
    }

    private fun updateSP(){
        val photosSP = getSharedPreferences(PHOTOS_SP, Context.MODE_PRIVATE)
        val prefsEditor = photosSP.edit()
        val gson = Gson()
        val jsonEntries = gson.toJson(entriesList)
        prefsEditor.putString(PHOTOS_SP_KEY, jsonEntries)
        prefsEditor.apply()
        sharedPrefsUpToDate = true
    }

    private fun saveEntry(entry: PhotoEntry){
        entriesList.add(entry)
        adapter?.notifyItemInserted(0)
        sharedPrefsUpToDate = false
    }
}