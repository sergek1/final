package com.sergek.yandex2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sergek.yandex2.classes.DB_classes.DB_main_stock
import com.sergek.yandex2.databinding.ActivityMainBinding
import fragments.Crypto_fragment
import fragments.FavouriteFragment
import fragments.StockFragment
import fragments.adapters.ViewPagerAdapter


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var db_main_stock: DB_main_stock
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager = findViewById(R.id.viewPager)
        tabs = findViewById(R.id.tabs)

        db_main_stock = DB_main_stock(this, null, 1)
        setUpTabs()
    }

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(StockFragment(), "Home")
        adapter.addFragment(Crypto_fragment(), "Crypto")
        adapter.addFragment(FavouriteFragment(), "Bookmarks")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }
}

