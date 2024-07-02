package com.webengage.demo.shopping

import android.app.Application
import com.webengage.sdk.android.WebEngage
import com.webengage.sdk.android.WebEngageConfig
//import com.webengage.personalization.WEPersonalization

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val webEngageConfig = WebEngageConfig.Builder()
            .setWebEngageKey("ksa~~47b66521")
            .setDebugMode(true)
            .build()
        WebEngage.engage(this, webEngageConfig)
        // Now initialise Personalization SDK
        WEPersonalization.get().init()
    }
}
