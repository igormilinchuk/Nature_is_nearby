package com.example.nature.data

import com.example.nature.data.remote.INaturalistApi
import com.example.nature.data.remote.ObservationDto
import com.example.nature.data.remote.ObservationsResponseDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ObservationRepositoryTest {

    private val fakeApi = object : INaturalistApi {
        override suspend fun getObservations(
            iconicTaxa: String?,
            latitude: Double,
            longitude: Double,
            radiusKm: Int,
            query: String?,
            searchOn: String?,
            withPhotos: Boolean,
            orderBy: String,
            perPage: Int,
            locale: String
        ) = ObservationsResponseDto(
            totalResults = 4,
            results = listOf(
                ObservationDto(id = 1),
                ObservationDto(id = 2),
                ObservationDto(id = 2),
                ObservationDto(id = 3)
            )
        )

        override suspend fun getObservation(id: Long, locale: String) =
            ObservationsResponseDto(results = listOf(ObservationDto(id = id)))
    }

    @Test
    fun observationIdsAreUniqueListKeys() = runBlocking {
        val observations = ObservationRepository(fakeApi)
            .getObservations(TaxonFilter.ALL, SearchArea.Default)

        val ids = observations.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
        assertEquals(listOf(1L, 2L, 3L), ids)
    }
}
