package com.example.mobiletestzemogamiguelpuentes.view.activities

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mobiletestzemogamiguelpuentes.databinding.ActivityMainBinding
import com.example.mobiletestzemogamiguelpuentes.databinding.FragmentAllPostsBinding
import com.example.mobiletestzemogamiguelpuentes.tools.Constants
import com.example.mobiletestzemogamiguelpuentes.view.adapters.SectionsPagerAdapter
import com.example.mobiletestzemogamiguelpuentes.view.fragments.AllPostsFragment
import com.example.mobiletestzemogamiguelpuentes.view.fragments.FavoritesFragment
import com.example.mobiletestzemogamiguelpuentes.viewmodel.PostsViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var allPostsFragment: AllPostsFragment = AllPostsFragment()
    private var favoritesFragment: FavoritesFragment = FavoritesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adapter initialization
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        sectionsPagerAdapter.addFragment(allPostsFragment,Constants.TITLE_FIRST_FRAGMENT)
        sectionsPagerAdapter.addFragment(favoritesFragment,Constants.TITLE_SECOND_FRAGMENT)

        // View pager initialization
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter


        // Set up with view pager
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
    }
}