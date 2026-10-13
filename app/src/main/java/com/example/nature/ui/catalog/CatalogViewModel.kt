package com.example.nature.ui.catalog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nature.data.ObservationRepository
import com.example.nature.data.SettingsRepository
import com.example.nature.data.TaxonFilter
import com.example.nature.data.settingsDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application.settingsDataStore)
    private val observationRepository = ObservationRepository()

    private val _uiState = MutableStateFlow(CatalogUiState())

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

    private var loadJob: Job? = null

    init {
        loadObservations()
    }

    fun onFilterSelected(filter: TaxonFilter) {
        if (filter == _uiState.value.selectedFilter && !_uiState.value.isError) return
        _uiState.update { it.copy(selectedFilter = filter) }
        loadObservations()
    }

    fun onShowAllClicked() {
        onFilterSelected(TaxonFilter.ALL)
    }

    fun onRetryClicked() {
        loadObservations()
    }

    private fun loadObservations() {
        loadJob?.cancel()
        val filter = _uiState.value.selectedFilter
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            try {
                val observations = observationRepository.getObservations(filter)
                _uiState.update { it.copy(observations = observations, isLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(observations = emptyList(), isLoading = false, isError = true) }
            }
        }
    }
}
