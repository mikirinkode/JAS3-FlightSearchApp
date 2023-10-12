package com.mikirinkode.flightsearchapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.mikirinkode.flightsearchapp.data.AppContainer
import com.mikirinkode.flightsearchapp.data.AppDataContainer
import com.mikirinkode.flightsearchapp.data.UserPreferenceRepository

class BaseApplication: Application() {

    lateinit var container: AppContainer
    lateinit var userPreferenceRepository: UserPreferenceRepository
    private val PREFERENCE_NAME = "user_preference"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREFERENCE_NAME
    )

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userPreferenceRepository = UserPreferenceRepository(dataStore)
    }
}