package com.example.nature.ui.detail

import com.example.nature.data.LoadError
import com.example.nature.data.Observation

data class ObservationDetailUiState(
    val observationId: Long,
    val isLoading: Boolean = true,
    val error: LoadError? = null,
    val observation: Observation? = null
)
