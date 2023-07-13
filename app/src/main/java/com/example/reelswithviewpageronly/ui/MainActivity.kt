package com.example.reelswithviewpageronly.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.reelswithviewpageronly.R
import com.example.reelswithviewpageronly.ui.content.adapters.MyFragmentStateAdapter
import com.example.reelswithviewpageronly.ui.models.ReelsFactory
import com.example.reelswithviewpageronly.ui.player.VideoCache
import java.io.File

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val requestCode: Int = 1010

    @SuppressLint("SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = createCardAdapter()


        // just to check if the cache folder is existed and can be accessed
        val cacheFolder = File("/data/user/0/com.example.reelswithviewpageronly/cache/exoCache")
        if (cacheFolder.exists() && cacheFolder.isDirectory) {
            // The cache folder exists and is accessible.
            Log.d(TAG, "onCreate: he cache folder exists and is accessible.")
        } else {
            // The cache folder does not exist or is not accessible.
            Log.d(TAG, "onCreate: The cache folder does not exist or is not accessible.")
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted. Perform the required operations.
            Log.d(TAG, "onCreate: Permission is already granted. Perform the required operations.")
        } else {
            Log.d(TAG, "onCreate: Permission is not granted. Request the permission.")
            // Permission is not granted. Request the permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requestCode
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (this.requestCode == requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted. Perform the required operations.
                Log.d(
                    TAG,
                    "onRequestPermissionsResult: Permission is granted. Perform the required operations."
                )
            } else {
                // Permission is denied. Handle the denial case.
                Log.d(
                    TAG,
                    "onRequestPermissionsResult: Permission is denied. Handle the denial case."
                )
            }
        }
    }

    private fun createCardAdapter(): MyFragmentStateAdapter {
        val reels = ReelsFactory.getReels()
        reels.addAll(ReelsFactory.getReels())
        return MyFragmentStateAdapter(this, reels)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        VideoCache.getInstance(this).release()
    }

    @SuppressLint("WrongThread")
    override fun onBackPressed() {
        super.onBackPressed()
        VideoCache.getInstance(this).release()
        Log.d(TAG, "onBackPressed: ")
    }

}