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
            description = null,
            uri = "https://www.inaturalist.org/observations/195067227",
            taxon = Taxon(
                id = 42069,
                name = "Vulpes vulpes",
                preferred_common_name = "Лисиця звичайна",
                rank = "species",
                iconic_taxon_name = "Mammalia",
                wikipedia_url = "https://en.wikipedia.org/wiki/Red_fox"
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
            description = null,
            uri = "https://www.inaturalist.org/observations/289097290",
            taxon = Taxon(
                id = 43048,
                name = "Erinaceus roumanicus",
                preferred_common_name = "Їжак білочеревий",
                rank = "species",
                iconic_taxon_name = "Mammalia",
                wikipedia_url = "https://en.wikipedia.org/wiki/Northern_white-breasted_hedgehog"
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
            description = null,
            uri = "https://www.inaturalist.org/observations/367997436",
            taxon = Taxon(
                id = 721186,
                name = "Ciconia ciconia ciconia",
                preferred_common_name = "Лелека білий звичайний",
                rank = "subspecies",
                iconic_taxon_name = "Aves",
                wikipedia_url = "https://en.wikipedia.org/wiki/White_stork"
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
            description = "Пара синиць годувалася насінням соняшнику на годівниці біля центральної алеї. Одна з птахів кілька хвилин сиділа на гілці липи й голосно співала, інша перелітала між кущами бузку.",
            uri = "",
            taxon = Taxon(
                id = 13094,
                name = "Parus major",
                preferred_common_name = "Синиця велика",
                rank = "species",
                iconic_taxon_name = "Aves",
                wikipedia_url = "https://en.wikipedia.org/wiki/Great_tit"
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
            description = "Метелик сідав на квіти моркви дикої на узліссі. Добре видно жовті крила з чорним візерунком, сині плями та червоні «вічка» біля хвостиків на задніх крилах. Сфотографовано близько полудня в сонячну погоду.",
            uri = "",
            taxon = Taxon(
                id = 58523,
                name = "Papilio machaon",
                preferred_common_name = "Махаон",
                rank = "species",
                iconic_taxon_name = "Insecta",
                wikipedia_url = "https://en.wikipedia.org/wiki/Papilio_machaon"
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
            description = "Кілька рослин на краю пшеничного поля вздовж ґрунтової дороги. Квітки яскраво-сині, кошики поодинокі, висота стебел близько 50 см.",
            uri = "",
            taxon = Taxon(
                id = 52821,
                name = "Centaurea cyanus",
                preferred_common_name = "Волошка синя",
                rank = "species",
                iconic_taxon_name = "Plantae",
                wikipedia_url = "https://en.wikipedia.org/wiki/Centaurea_cyanus"
            ),
            photos = listOf(
                Photo(id = 900000103, url = "", localRes = R.drawable.obs_cornflower)
            )
        )
    )

    fun findById(id: Long): Observation? = items.firstOrNull { it.id == id }
}
