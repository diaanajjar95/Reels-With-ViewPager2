package com.example.reelswithviewpageronly.ui.content.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.reelswithviewpageronly.ui.models.ReelItem
import com.example.reelswithviewpageronly.ui.content.ContentFragment

class MyFragmentStateAdapter(
    fragmentActivity: FragmentActivity,
    private val reels: List<ReelItem>
) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return ContentFragment.newInstance(
            video = reels[position].url,
            title = reels[position].title
        )
    }

    override fun getItemCount(): Int {
        return reels.size
    }
}