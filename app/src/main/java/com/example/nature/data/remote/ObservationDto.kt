package com.example.nature.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ObservationsResponseDto(
    @SerialName("total_results") val totalResults: Int = 0,
    val results: List<ObservationDto> = emptyList()
)

@Serializable
data class ObservationDto(
    val id: Long,
    @SerialName("species_guess") val speciesGuess: String? = null,
    @SerialName("place_guess") val placeGuess: String? = null,
    @SerialName("observed_on") val observedOn: String? = null,
    val location: String? = null,
    @SerialName("quality_grade") val qualityGrade: String? = null,
    val description: String? = null,
    val uri: String? = null,
    val taxon: TaxonDto? = null,
    val photos: List<PhotoDto> = emptyList()
)

@Serializable
data class TaxonDto(
    val id: Long,
    val name: String? = null,
    @SerialName("preferred_common_name") val preferredCommonName: String? = null,
    val rank: String? = null,
    @SerialName("iconic_taxon_name") val iconicTaxonName: String? = null,
    @SerialName("wikipedia_url") val wikipediaUrl: String? = null
)

@Serializable
data class PhotoDto(
    val id: Long? = null,
    val url: String? = null
)
