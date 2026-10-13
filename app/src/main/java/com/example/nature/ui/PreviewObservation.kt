package com.example.nature.ui

import com.example.nature.data.Observation
import com.example.nature.data.Taxon

internal val previewObservation = Observation(
    id = 195067227,
    speciesGuess = "Лисиця звичайна",
    placeGuess = "Канівський район, Черкаська область, Україна",
    observedOn = "2023-12-28",
    location = "49.7247061119,31.525734026",
    qualityGrade = "research",
    description = null,
    uri = "https://www.inaturalist.org/observations/195067227",
    taxon = Taxon(
        id = 42069,
        name = "Vulpes vulpes",
        preferredCommonName = "Лисиця звичайна",
        rank = "species",
        iconicTaxonName = "Mammalia",
        wikipediaUrl = "https://en.wikipedia.org/wiki/Red_fox"
    ),
    photoUrl = null
)
