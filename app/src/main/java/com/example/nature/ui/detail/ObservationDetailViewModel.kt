package com.example.nature.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.nature.data.SampleObservations
import com.example.nature.ui.navigation.NatureDestinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ObservationDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val observationId: Long = savedStateHandle.get<Long>(NatureDestinations.OBSERVATION_ID) ?: -1L

    private val _uiState = MutableStateFlow(
        ObservationDetailUiState(
            observationId = observationId,
            observation = SampleObservations.findById(observationId)
        )
    )
    val uiState: StateFlow<ObservationDetailUiState> = _uiState.asStateFlow()
}
