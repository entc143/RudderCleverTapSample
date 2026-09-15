package com.example.rudderclevertapsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rudderclevertapsample.databinding.ActivityWelcomeBinding

/** Shown after the Event button sends a page-load event to CleverTap. */
class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
