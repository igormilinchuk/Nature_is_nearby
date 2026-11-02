package com.example.nature.ui.catalog

import com.example.nature.data.LoadError
import com.example.nature.data.Observation
import com.example.nature.data.SearchArea
import com.example.nature.data.SettingsRepository
import com.example.nature.data.TaxonFilter

sealed interface CatalogContent {
    data object Loading : CatalogContent
    data class Error(val error: LoadError) : CatalogContent
    data object Empty : CatalogContent
    data class Success(val observations: List<Observation>) : CatalogContent
}

enum class LocationMessage {
    PERMISSION_DENIED,
    UNAVAILABLE
}

data class CatalogUiState(
    val content: CatalogContent = CatalogContent.Loading,
    val filters: List<TaxonFilter> = TaxonFilter.entries,
    val selectedFilter: TaxonFilter = TaxonFilter.ALL,
    val searchArea: SearchArea = SearchArea.Default,
    val isLocating: Boolean = false,
    val locationMessage: LocationMessage? = null,
    val showScientificNames: Boolean = SettingsRepository.DEFAULT_SHOW_SCIENTIFIC_NAMES
)
