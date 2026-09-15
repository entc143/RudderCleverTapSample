package com.example.rudderclevertapsample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rudderclevertapsample.databinding.ActivityMainBinding
import com.rudderstack.integration.kotlin.clevertap.CleverTapIntegration
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("ClevertapRudderstack", "Notification permission granted by user.")
        } else {
            Log.w("ClevertapRudderstack", "Notification permission denied by user.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request runtime permission for Push Notifications on Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Handle the incoming intent (push notification or deep link) with security hardening
        handleIntent(intent)

        val anonymousId = MyApplication.analytics.anonymousId
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("ClevertapRudderstack", "Rudderstack anonymousId: $anonymousId")
        Log.d("ClevertapRudderstack", "Rudderstack hardware deviceId (ANDROID_ID): $androidId")

        // Button 1 - Identify: sends user identity to CleverTap (device mode) via RudderStack,
        // then opens the Thank You screen.
        binding.buttonIdentify.setOnClickListener {
            MyApplication.analytics.identify(
                userId = "RS_user_1",
                traits = buildJsonObject {
                    put("name", "RS User")
                    put("email", "rs@example.com")
                    put("phone", "+14155551234")
                }
            )

            startActivity(Intent(this, ThankYouActivity::class.java))
        }

        // Button 2 - Event: sends a page-load event to CleverTap (device mode) via RudderStack,
        // then opens the Welcome to RudderStack<>CleverTap screen.
        binding.buttonEvent.setOnClickListener {
            MyApplication.analytics.track(
                name = "custom_Page_Loaded",
                properties = buildJsonObject {
                    put("RSpage_name", "Welcome to RudderStack<>CleverTap")
                    put("RSsource", "Event button")
                }
            )

            startActivity(Intent(this, WelcomeActivity::class.java))
        }

        // AddtoCart Button: sends event to RudderStack with name 'AddtoCart'
        binding.buttonAddToCart.setOnClickListener {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            MyApplication.analytics.track(
                name = "AddtoCart",
                properties = buildJsonObject {
                    put("productAdate", currentDate)
                    put("amount", 29.99)
                    put("quantity", 2)
                    put("name", "Awesome Shoes")
                }
            )
        }

        // ProductView Button: sends event to RudderStack with name 'ProductView' and properties
        binding.buttonProductView.setOnClickListener {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            MyApplication.analytics.track(
                name = "ProductView",
                properties = buildJsonObject {
                    put("productVdate", currentDate)
                    put("amount", 29.99)
                    put("quantity", 2)
                    put("name", "Awesome Shoes")
                }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        // Handle the incoming intent with security hardening
        handleIntent(intent)
    }

    /**
     * Handles the incoming intent (push notification or deep link) and forwards it to 
     * the CleverTap integration. Accesses extras to satisfy Android 16+ intent hardening.
     */
    private fun handleIntent(intent: Intent?) {
        Log.d("ClevertapRudderstack", "handleIntent called with action: ${intent?.action}, data: ${intent?.data}")
        intent?.let {
            val extras = it.extras
            if (extras != null) {
                Log.d("ClevertapRudderstack", "Intent extras keys: ${extras.keySet().joinToString()}")
                for (key in extras.keySet()) {
                    Log.d("ClevertapRudderstack", "  $key = ${extras.get(key)}")
                }
            } else {
                Log.d("ClevertapRudderstack", "Intent extras is null")
            }

            // Triggering unparcelling of extras helps the system "collect" nested keys,
            // mitigating "IntentRedirect Hardening" warnings on some devices/Android 16+.
            try {
                extras?.let { bundle ->
                    // Touch all primary keys
                    bundle.keySet()
                    // Deep unparcel any nested bundles or intent extras if they exist
                    for (key in bundle.keySet()) {
                        try {
                            val value = bundle.get(key)
                            if (value is Bundle) {
                                value.keySet() // Force collection of nested keys
                            } else if (value is Intent) {
                                value.extras?.keySet() // Force collection of nested intent keys
                            }
                        } catch (_: Exception) {}
                    }
                }
            } catch (e: Exception) {
                Log.w("ClevertapRudderstack", "Failed to unparcel extras: ${e.message}")
            }

            val cleverTapAPI = MyApplication.cleverTapIntegration.getDestinationInstance()
            Log.d("ClevertapRudderstack", "CleverTap destination instance initialized: ${cleverTapAPI != null}")

            // Forward deep link and notification click
            Log.d("ClevertapRudderstack", "Forwarding pushNotificationClickedEvent to integration")
            MyApplication.cleverTapIntegration.pushNotificationClickedEvent(extras)
            
            if (it.data != null) {
                Log.d("ClevertapRudderstack", "Forwarding pushDeepLink to integration: ${it.data}")
                MyApplication.cleverTapIntegration.pushDeepLink(it.data)
            }
        }
    }
}
