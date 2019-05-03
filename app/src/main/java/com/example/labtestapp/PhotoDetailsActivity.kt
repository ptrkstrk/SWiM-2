package com.example.labtestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.labtestapp.fragments.DetailsAndLookalikesFragment
import com.example.labtestapp.fragments.PhotoFragment
import com.google.android.material.tabs.TabLayout

class PhotoDetailsActivity : AppCompatActivity() {

    companion object {
        const val URL_KEY = "url"
        const val NAME_KEY = "name"
        const val TAGS_KEY = "tags"
        const val SIMILAR_PHOTOS_URLS_KEY = "similar photos"
        const val DATE_KEY = "date"
    }

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var date = ""
    private var name = ""
    private var photoURL = ""
    private var tags = ""
    private lateinit var similarUrls:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setValuesFromIntent()
        prepareViewPager()
        prepareTabLayout()
    }

    private fun setValuesFromIntent() {
        name = intent.getStringExtra(NAME_KEY)
        date = intent.getStringExtra(DATE_KEY)
        photoURL = intent.getStringExtra(URL_KEY)
        tags = intent.getStringExtra(TAGS_KEY)
        similarUrls = intent.getStringArrayListExtra(SIMILAR_PHOTOS_URLS_KEY)
    }

    private fun prepareViewPager() {
        viewPager = findViewById(R.id.viewPager)
        val pagerAdapter = DetailsSlidePagerAdapter(supportFragmentManager)
        val photoFragment = PhotoFragment.newInstance(photoURL)
        val detailsAndLookalikesFragment = DetailsAndLookalikesFragment.newInstance(name, date, tags, similarUrls)
        pagerAdapter.addPage(photoFragment, getString(R.string.photo_page_title))
        pagerAdapter.addPage(detailsAndLookalikesFragment, getString(R.string.details_page_title))

        viewPager.adapter = pagerAdapter
    }

    private fun prepareTabLayout() {
        tabLayout = findViewById(R.id.photos_tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    private inner class DetailsSlidePagerAdapter(fragmentM: FragmentManager) : FragmentPagerAdapter(fragmentM) {
        val fragments: ArrayList<Pair<Fragment, String>> = ArrayList()

        override fun getCount(): Int = fragments.size

        override fun getItem(position: Int): Fragment {
            return fragments[position].first
        }

        override fun getPageTitle(position: Int):CharSequence?{
            return fragments[position].second
        }

        fun addPage(fragment: Fragment, title: String){
            fragments.add(Pair(fragment, title))
        }
    }

}