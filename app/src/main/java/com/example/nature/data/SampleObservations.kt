package com.example.nature.data

import com.example.nature.R

object SampleObservations {

    val items: List<Observation> = listOf(
        Observation(
            id = 195067227,
            species_guess = "Лисиця звичайна",
            place_guess = "Канівський район, Черкаська область, Україна",
            observed_on = "2023-12-28",
            location = "49.7247061119,31.525734026",
            quality_grade = "research",
            taxon = Taxon(
                id = 42069,
                name = "Vulpes vulpes",
                preferred_common_name = "Лисиця звичайна",
                rank = "species",
                iconic_taxon_name = "Mammalia"
            ),
            photos = listOf(
                Photo(
                    id = 343070514,
                    url = "https://inaturalist-open-data.s3.amazonaws.com/photos/343070514/square.jpg",
                    localRes = R.drawable.obs_fox
                )
            )
        ),
        Observation(
            id = 289097290,
            species_guess = "Їжак Білочеревий Північний",
            place_guess = "Мотовилівка, Київська область, Україна",
            observed_on = "2025-06-10",
            location = "50.158988843,30.062780343",
            quality_grade = "research",
            taxon = Taxon(
                id = 43048,
                name = "Erinaceus roumanicus",
                preferred_common_name = "Їжак білочеревий",
                rank = "species",
                iconic_taxon_name = "Mammalia"
            ),
            photos = listOf(
                Photo(
                    id = 519943438,
                    url = "https://inaturalist-open-data.s3.amazonaws.com/photos/519943438/square.jpg",
                    localRes = R.drawable.obs_hedgehog
                )
            )
        ),
        Observation(
            id = 367997436,
            species_guess = "Лелека білий (номінативний)",
            place_guess = "Змітнів, Чернігівська область, Україна",
            observed_on = "2026-06-03",
            location = "51.5878909442,32.7282232046",
            quality_grade = "research",
            taxon = Taxon(
                id = 721186,
                name = "Ciconia ciconia ciconia",
                preferred_common_name = "Лелека білий звичайний",
                rank = "subspecies",
                iconic_taxon_name = "Aves"
            ),
            photos = listOf(
                Photo(
                    id = 672093850,
                    url = "https://inaturalist-open-data.s3.amazonaws.com/photos/672093850/square.jpg",
                    localRes = R.drawable.obs_stork
                )
            )
        ),
        Observation(
            id = 900000001,
            species_guess = "Синиця велика",
            place_guess = "Голосіївський парк, Київ, Україна",
            observed_on = "2026-09-14",
            location = "50.3920,30.5160",
            quality_grade = "research",
            taxon = Taxon(
                id = 13094,
                name = "Parus major",
                preferred_common_name = "Синиця велика",
                rank = "species",
                iconic_taxon_name = "Aves"
            ),
            photos = listOf(
                Photo(id = 900000101, url = "", localRes = R.drawable.obs_tit)
            )
        ),
        Observation(
            id = 900000002,
            species_guess = "Махаон",
            place_guess = "Канівський заповідник, Черкаська область, Україна",
            observed_on = "2026-07-21",
            location = "49.7350,31.5050",
            quality_grade = "needs_id",
            taxon = Taxon(
                id = 58523,
                name = "Papilio machaon",
                preferred_common_name = "Махаон",
                rank = "species",
                iconic_taxon_name = "Insecta"
            ),
            photos = listOf(
                Photo(id = 900000102, url = "", localRes = R.drawable.obs_swallowtail)
            )
        ),
        Observation(
            id = 900000003,
            species_guess = "Волошка синя",
            place_guess = "Васильків, Київська область, Україна",
            observed_on = "2026-06-30",
            location = "50.1800,30.3200",
            quality_grade = "casual",
            taxon = Taxon(
                id = 52821,
                name = "Centaurea cyanus",
                preferred_common_name = "Волошка синя",
                rank = "species",
                iconic_taxon_name = "Plantae"
            ),
            photos = listOf(
                Photo(id = 900000103, url = "", localRes = R.drawable.obs_cornflower)
            )
        )
    )
}
