package com.mikirinkode.flightsearchapp

import android.app.Application
import com.mikirinkode.flightsearchapp.data.AppContainer
import com.mikirinkode.flightsearchapp.data.AppDataContainer

class BaseApplication: Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}