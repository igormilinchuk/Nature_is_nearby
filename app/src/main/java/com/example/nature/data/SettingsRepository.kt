package com.example.nature.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {

    val showScientificNames: Flow<Boolean> = dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences ->
            preferences[SHOW_SCIENTIFIC_NAMES] ?: DEFAULT_SHOW_SCIENTIFIC_NAMES
        }

    suspend fun setShowScientificNames(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_SCIENTIFIC_NAMES] = value
        }
    }

    companion object {
        const val DEFAULT_SHOW_SCIENTIFIC_NAMES = false
        private val SHOW_SCIENTIFIC_NAMES = booleanPreferencesKey("show_scientific_names")
    }
}
