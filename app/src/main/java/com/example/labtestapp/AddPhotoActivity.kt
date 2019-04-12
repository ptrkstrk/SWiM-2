package com.example.labtestapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_photo.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import java.text.DateFormat

class AddPhotoActivity : AppCompatActivity() {

    companion object {
        const val PHOTO_NAME_KEY = "photoNameKey"
        const val PHOTO_URL_KEY = "photoUrlKey"
        const val PHOTO_DATE_KEY = "photoDateKey"
        const val NAME_MAX_LENGTH = 25
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        add_button.setOnClickListener {
            proceedToAddingPhoto()
        }
    }

    private fun proceedToAddingPhoto() {
        val additionReady = verifyInputs()
        if (additionReady) {
            prepareAndSetResult()
            finish()
        }
    }

    private fun prepareAndSetResult() {
        val photoIntent = Intent()
        val dateInstance =  DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        val date = dateInstance.format(Date().time)

        photoIntent.putExtra(PHOTO_NAME_KEY, nameET.text.toString())
        photoIntent.putExtra(PHOTO_URL_KEY, urlET.text.toString())
        photoIntent.putExtra(PHOTO_DATE_KEY, date)

        setResult(Activity.RESULT_OK, photoIntent)
    }

    private fun verifyInputs(): Boolean {
        val validURL = verifyURLInput()
        val validName = verifyNameInput()
        return validURL && validName
    }

    private fun verifyNameInput(): Boolean {
        val noNameProvided = nameET.text.isEmpty()
        if (noNameProvided) {
            nameET.error = getString(R.string.add_no_name_error_msg)
        }
        val tooLongName = nameET.text.toString().length > NAME_MAX_LENGTH
        if (tooLongName) {
            nameET.error = getString(R.string.add_long_name_error_msg)
        }
        return !noNameProvided && !tooLongName
    }

    private fun verifyURLInput(): Boolean {
        val invalidURL = urlET.text.isEmpty()
        if (invalidURL) {
            urlET.error = getString(R.string.add_url_error_msg)
            return false
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
