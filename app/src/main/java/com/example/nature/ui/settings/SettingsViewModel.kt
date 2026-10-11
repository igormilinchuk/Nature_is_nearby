package com.example.nature.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nature.data.SettingsRepository
import com.example.nature.data.settingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application.settingsDataStore)

    val showScientificNames: StateFlow<Boolean> = settingsRepository.showScientificNames
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsRepository.DEFAULT_SHOW_SCIENTIFIC_NAMES
        )

    fun onShowScientificNamesChange(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShowScientificNames(value)
        }
    }
}
