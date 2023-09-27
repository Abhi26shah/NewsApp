package com.example.newsapp.utility

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.BuildConfig
import com.google.firebase.FirebaseApp

class NewsApplication: Application(), LifecycleObserver {

    companion object {
        @JvmStatic
        var appContext: NewsApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        init(context = this)
        if (!BuildConfig.DEBUG){
            FirebaseApp.initializeApp(this)
        }

//        FirebaseApp.initializeApp(this)
        registerActivityLifecycleCallbacks(AppLifecycleTracker())
    }

    private fun init(context: NewsApplication) {
        appContext = context
    }
}