package com.example.nature.data.remote

import com.example.nature.data.Observation
import com.example.nature.data.Taxon

fun ObservationDto.toDomain(): Observation = Observation(
    id = id,
    speciesGuess = speciesGuess.orNullIfBlank(),
    placeGuess = placeGuess.orNullIfBlank(),
    observedOn = observedOn.orNullIfBlank(),
    location = location.orNullIfBlank(),
    qualityGrade = qualityGrade.orNullIfBlank() ?: "casual",
    description = description.orNullIfBlank(),
    uri = uri.orNullIfBlank(),
    taxon = taxon?.toDomain(),
    photoUrl = photos.firstNotNullOfOrNull { it.url.orNullIfBlank() }
        ?.replace("/square.", "/medium.")
)

fun TaxonDto.toDomain(): Taxon? {
    val scientificName = name.orNullIfBlank() ?: return null
    return Taxon(
        id = id,
        name = scientificName,
        preferredCommonName = preferredCommonName.orNullIfBlank(),
        rank = rank.orNullIfBlank(),
        iconicTaxonName = iconicTaxonName.orNullIfBlank(),
        wikipediaUrl = wikipediaUrl.orNullIfBlank()
    )
}

private fun String?.orNullIfBlank(): String? = this?.takeIf { it.isNotBlank() }
