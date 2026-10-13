package com.example.nature.data

data class Observation(
    val id: Long,
    val speciesGuess: String?,
    val placeGuess: String?,
    val observedOn: String?,
    val location: String?,
    val qualityGrade: String,
    val description: String?,
    val uri: String?,
    val taxon: Taxon?,
    val photoUrl: String?
)

data class Taxon(
    val id: Long,
    val name: String,
    val preferredCommonName: String?,
    val rank: String?,
    val iconicTaxonName: String?,
    val wikipediaUrl: String?
)

val Observation.commonName: String?
    get() = taxon?.preferredCommonName ?: speciesGuess

val Observation.scientificName: String?
    get() = taxon?.name
