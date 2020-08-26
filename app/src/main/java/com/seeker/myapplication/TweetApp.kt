package com.seeker.myapplication

import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco

class TweetApp : Application() {
    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
    }

    override fun onCreate() {
        applnstance = this
        super.onCreate()
        Fresco.initialize(this)
    }

    companion object {
        var applnstance: TweetApp? = null
    }
}