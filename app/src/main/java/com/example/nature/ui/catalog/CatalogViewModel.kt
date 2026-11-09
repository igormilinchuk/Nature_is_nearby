package com.example.nature.ui.catalog

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay
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

    var searchQuery by mutableStateOf("")
        private set

    private var loadJob: Job? = null
    private var countsJob: Job? = null
    private var searchJob: Job? = null

    init {
        loadObservations()
        loadCounts()
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
        searchJob?.cancel()
        if (query.isBlank()) {
            loadObservations()
            return
        }
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            loadObservations()
        }
    }

    fun onSearchSubmit() {
        searchJob?.cancel()
        loadObservations()
    }

    fun onClearSearch() {
        onSearchQueryChange("")
    }

    fun onRefresh() {
        loadObservations(isRefresh = true)
        loadCounts()
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
                loadCounts()
            }
        }
    }

    private fun loadObservations(isRefresh: Boolean = false) {
        loadJob?.cancel()
        val filter = _uiState.value.selectedFilter
        val area = _uiState.value.searchArea
        val query = searchQuery.trim()
        loadJob = viewModelScope.launch {
            _uiState.update {
                if (isRefresh) it.copy(isRefreshing = true) else it.copy(content = CatalogContent.Loading)
            }
            val content = try {
                val observations = if (query.isEmpty()) {
                    observationRepository.getObservations(filter, area)
                } else {
                    observationRepository.getObservations(filter, area, query)
                }
                when {
                    observations.isNotEmpty() -> CatalogContent.Success(observations)
                    query.isEmpty() -> CatalogContent.Empty
                    else -> CatalogContent.NoSearchResults(query)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                CatalogContent.Error(e.toLoadError())
            }
            _uiState.update { it.copy(content = content, isRefreshing = false) }
        }
    }

    private fun loadCounts() {
        countsJob?.cancel()
        val filters = _uiState.value.filters
        val area = _uiState.value.searchArea
        countsJob = viewModelScope.launch {
            val counts = observationRepository.getCounts(filters, area)
            _uiState.update { it.copy(counts = counts) }
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_MS = 500L
    }
}
