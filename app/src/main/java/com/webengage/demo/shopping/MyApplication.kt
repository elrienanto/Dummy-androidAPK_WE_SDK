package com.webengage.demo.shopping

import android.app.Application
import com.webengage.sdk.android.WebEngage
import com.webengage.sdk.android.WebEngageConfig
import com.webengage.sdk.android.WebEngageActivityLifeCycleCallbacks

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val webEngageConfig = WebEngageConfig.Builder()
            .setWebEngageKey("ksa~~47b66521") // Make sure to use the key as a string
            .setDebugMode(true) // only in development mode
            .build()

        WebEngage.engage(this, webEngageConfig)

        registerActivityLifecycleCallbacks(
            WebEngageActivityLifeCycleCallbacks(
                this,
                webEngageConfig
            )
        )
    }
}
