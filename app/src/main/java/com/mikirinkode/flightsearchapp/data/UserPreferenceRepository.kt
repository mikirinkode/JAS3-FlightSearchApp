package com.mikirinkode.flightsearchapp.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mikirinkode.flightsearchapp.ui.viewmodel.FlightViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class UserPreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val USER_SEARCH_QUERY = stringPreferencesKey("user_search_query")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveUserSearchQuery(query: String) {
        Log.e(TAG, "Save query called, query is: ${query}")
        dataStore.edit { preferences ->
            preferences[USER_SEARCH_QUERY] = query
            Log.e(TAG, "query success saved")
        }
    }

    val lastQuery: Flow<String?> = dataStore.data.map {
        it[USER_SEARCH_QUERY]
    }
    val userSearchQuery: Flow<String> = dataStore.data
        .catch {
            Log.e(TAG, "Error reading preferences.", it)
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
                Log.e(TAG, "Error reading preferences.", it)
            }
        }
        .map { preferences ->
            Log.e(TAG, "success reading preferences.")
            preferences[USER_SEARCH_QUERY] ?: ""
        }

}