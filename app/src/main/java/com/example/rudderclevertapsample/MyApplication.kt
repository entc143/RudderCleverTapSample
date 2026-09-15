package com.example.rudderclevertapsample

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.app.NotificationManager
import com.clevertap.android.sdk.CleverTapAPI
import com.rudderstack.integration.kotlin.clevertap.CleverTapIntegration
import com.rudderstack.sdk.kotlin.android.Analytics
import com.rudderstack.sdk.kotlin.android.Configuration
import com.rudderstack.sdk.kotlin.core.internals.logger.Logger
import kotlinx.serialization.json.buildJsonObject

/**
 * Application class responsible for:
 * Replace WRITE_KEY and DATA_PLANE_URL below with the values from your RudderStack source,
 * and configure the CleverTap destination (Account ID / Account Token / Region) in the
 * RudderStack dashboard - the integration reads those from there automatically.
 */
class MyApplication : Application() {
    companion object {
        private const val WRITE_KEY = "3JJ3c1fMq0hkH202I6fCMaTAmRW"
        private const val DATA_PLANE_URL = "https://clevertapvhcnymhkt.dataplane.rudderstack.com"
        lateinit var analytics: Analytics
        lateinit var cleverTapIntegration: CleverTapIntegration
    }

    override fun onCreate() {
        super.onCreate()

        analytics = Analytics(
            configuration = Configuration(
                writeKey = WRITE_KEY,
                application = this,
                dataPlaneUrl = DATA_PLANE_URL,
                // Automatically tracks Application Installed / Opened / Updated on launch,
                // and forwards Activity lifecycle callbacks needed by CleverTap in-app messages.
                trackActivities = true,
                logLevel = Logger.LogLevel.VERBOSE
            )
        )

        // analytics.identify()/track()/screen() call is also delivered directly to the
        // CleverTap SDK running inside this app.
        cleverTapIntegration = CleverTapIntegration()

        analytics.add(cleverTapIntegration)
       /* CleverTapAPI.createNotificationChannel(
            applicationContext,
            "ShubPN",
            "ShubPN",
            "Your Channel Description",
            NotificationManager.IMPORTANCE_MAX,
            true
        )*/

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                val cleverTapAPI = cleverTapIntegration.getDestinationInstance() as? CleverTapAPI
                if (cleverTapAPI != null) {


                    CleverTapAPI.createNotificationChannel(
                        applicationContext,
                        "ShubPN",
                        "ShubPN",
                        "Your Channel Description",
                        NotificationManager.IMPORTANCE_MAX,
                        true
                    )


                    val cleverTapID = cleverTapAPI.cleverTapID
                    Log.d("ClevertapRudderstack", "CleverTap ID fetched successfully: $cleverTapID")
                    
                    // Fetch the FCM token and register it to CleverTap & RudderStack

                    com.google.firebase.messaging.FirebaseMessaging.getInstance().token
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val token = task.result
                                Log.d("ClevertapRudderstack", "FCM token generated successfully: $token")
                                
                                // 1. Pass the token to CleverTap directly
                                cleverTapAPI.pushFcmRegistrationId(token, true)

                            } else {
                                Log.e("ClevertapRudderstack", "FCM token generation failed", task.exception)
                            }
                        }
                } else {
                    // Try again in 200 milliseconds if CleverTap destination hasn't initialized yet
                    handler.postDelayed(this, 200)
                }
            }
        })




    }


}




