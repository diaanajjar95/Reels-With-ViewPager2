package com.example.reelswithviewpageronly.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.reelswithviewpageronly.ui.content.adapters.MyFragmentStateAdapter
import com.example.reelswithviewpageronly.R
import com.example.reelswithviewpageronly.ui.models.ReelsFactory

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