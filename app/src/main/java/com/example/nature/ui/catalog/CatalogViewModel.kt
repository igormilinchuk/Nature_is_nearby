package com.example.nature.ui.catalog

import androidx.lifecycle.ViewModel
import com.example.nature.data.SampleObservations
import com.example.nature.data.TaxonFilter
import com.example.nature.data.filterBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CatalogViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        CatalogUiState(
            observations = SampleObservations.items,
            visibleObservations = SampleObservations.items
        )
    )
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

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
