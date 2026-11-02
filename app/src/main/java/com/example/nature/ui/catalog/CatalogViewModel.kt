package com.example.nature.ui.catalog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nature.data.LocationProvider
import com.example.nature.data.ObservationRepository
import com.example.nature.data.SearchArea
import com.example.nature.data.SettingsRepository
import com.example.nature.data.TaxonFilter
import com.example.nature.data.settingsDataStore
import com.example.nature.data.toLoadError
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
    private val locationProvider = LocationProvider(application)

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
        if (filter == _uiState.value.selectedFilter && _uiState.value.content !is CatalogContent.Error) return
        _uiState.update { it.copy(selectedFilter = filter) }
        loadObservations()
    }

    fun onShowAllClicked() {
        onFilterSelected(TaxonFilter.ALL)
    }

    fun onRetryClicked() {
        loadObservations()
    }

    fun onLocationPermissionResult(granted: Boolean) {
        if (!granted) {
            _uiState.update { it.copy(locationMessage = LocationMessage.PERMISSION_DENIED) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLocating = true, locationMessage = null) }
            val location = try {
                locationProvider.currentLocation()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                null
            }
            if (location == null) {
                _uiState.update { it.copy(isLocating = false, locationMessage = LocationMessage.UNAVAILABLE) }
            } else {
                _uiState.update {
                    it.copy(
                        isLocating = false,
                        searchArea = SearchArea(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            isUserLocation = true
                        )
                    )
                }
                loadObservations()
            }
        }
    }

    private fun loadObservations() {
        loadJob?.cancel()
        val filter = _uiState.value.selectedFilter
        val area = _uiState.value.searchArea
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(content = CatalogContent.Loading) }
            val content = try {
                val observations = observationRepository.getObservations(filter, area)
                if (observations.isEmpty()) CatalogContent.Empty else CatalogContent.Success(observations)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                CatalogContent.Error(e.toLoadError())
            }
            _uiState.update { it.copy(content = content) }
        }
    }
}
