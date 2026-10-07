package com.example.nature.ui.detail

import com.example.nature.data.Observation

data class ObservationDetailUiState(
    val observationId: Long,
    val observation: Observation?
)
