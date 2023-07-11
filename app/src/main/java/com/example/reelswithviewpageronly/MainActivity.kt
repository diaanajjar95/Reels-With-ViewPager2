package com.example.reelswithviewpageronly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = createCardAdapter()
    }

    private fun createCardAdapter(): MyFragmentStateAdapter {
        return MyFragmentStateAdapter(this, ReelsFactory.getReels())
    }
}