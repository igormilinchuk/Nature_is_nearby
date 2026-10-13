package com.example.nature.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nature.data.ObservationRepository
import com.example.nature.ui.navigation.NatureDestinations
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ObservationDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val observationId: Long = savedStateHandle.get<Long>(NatureDestinations.OBSERVATION_ID) ?: -1L
    private val observationRepository = ObservationRepository()

    private val _uiState = MutableStateFlow(ObservationDetailUiState(observationId = observationId))
    val uiState: StateFlow<ObservationDetailUiState> = _uiState.asStateFlow()

    init {
        loadObservation()
    }

    fun onRetryClicked() {
        loadObservation()
    }

    private fun loadObservation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            try {
                val observation = observationRepository.getObservation(observationId)
                _uiState.update { it.copy(isLoading = false, observation = observation) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }
}
