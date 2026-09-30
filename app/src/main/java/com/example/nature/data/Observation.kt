package com.example.nature.data

import androidx.annotation.DrawableRes

data class Observation(
    val id: Long,
    val species_guess: String,
    val place_guess: String,
    val observed_on: String,
    val location: String,
    val quality_grade: String,
    val taxon: Taxon,
    val photos: List<Photo>
)

data class Taxon(
    val id: Long,
    val name: String,
    val preferred_common_name: String,
    val rank: String,
    val iconic_taxon_name: String
)

data class Photo(
    val id: Long,
    val url: String,
    @DrawableRes val localRes: Int
)
