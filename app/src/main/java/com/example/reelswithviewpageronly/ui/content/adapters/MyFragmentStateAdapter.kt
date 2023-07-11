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
//            video = reels[position].url,
            video = "https://s3.amazonaws.com/orion360-us/Orion360_test_video_2d_equi_360x180deg_1920x960pix_30fps_30sec_x264.mp4",
            title = reels[position].title
        )
    }

    override fun getItemCount(): Int {
        return reels.size
    }
}