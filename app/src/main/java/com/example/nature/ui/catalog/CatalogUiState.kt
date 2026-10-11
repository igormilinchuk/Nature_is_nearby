package com.example.nature.ui.catalog

import com.example.nature.data.Observation
import com.example.nature.data.SettingsRepository
import com.example.nature.data.TaxonFilter

data class CatalogUiState(
    val observations: List<Observation> = emptyList(),
    val filters: List<TaxonFilter> = TaxonFilter.entries,
    val selectedFilter: TaxonFilter = TaxonFilter.ALL,
    val visibleObservations: List<Observation> = observations,
    val showScientificNames: Boolean = SettingsRepository.DEFAULT_SHOW_SCIENTIFIC_NAMES
)
