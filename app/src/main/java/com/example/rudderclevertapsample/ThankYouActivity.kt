package com.example.rudderclevertapsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rudderclevertapsample.databinding.ActivityThankYouBinding

/** Shown after the Identify button sends the user's identity to CleverTap. */
class ThankYouActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityThankYouBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
