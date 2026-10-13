package com.example.nature.ui.detail

import com.example.nature.data.Observation

data class ObservationDetailUiState(
    val observationId: Long,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val observation: Observation? = null
)
