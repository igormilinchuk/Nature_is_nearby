package com.example.nature.ui.catalog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nature.data.SampleObservations
import com.example.nature.data.SettingsRepository
import com.example.nature.data.TaxonFilter
import com.example.nature.data.filterBy
import com.example.nature.data.settingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CatalogViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application.settingsDataStore)

    private val _uiState = MutableStateFlow(
        CatalogUiState(
            observations = SampleObservations.items,
            visibleObservations = SampleObservations.items
        )
    )

    val uiState: StateFlow<CatalogUiState> = combine(
        _uiState,
        settingsRepository.showScientificNames
    ) { state, showScientificNames ->
        state.copy(showScientificNames = showScientificNames)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = _uiState.value
    )

    fun onFilterSelected(filter: TaxonFilter) {
        _uiState.update { state ->
            state.copy(
                selectedFilter = filter,
                visibleObservations = state.observations.filterBy(filter)
            )
        }
    }

    fun onShowAllClicked() {
        onFilterSelected(TaxonFilter.ALL)
    }
}
