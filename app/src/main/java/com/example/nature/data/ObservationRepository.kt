package com.example.nature.data

import com.example.nature.data.remote.INaturalistApi
import com.example.nature.data.remote.NetworkModule
import com.example.nature.data.remote.toDomain

class ObservationRepository(
    private val api: INaturalistApi = NetworkModule.api
) {

    suspend fun getObservations(filter: TaxonFilter): List<Observation> =
        api.getObservations(
            iconicTaxa = filter.iconicTaxonName,
            latitude = SEARCH_LATITUDE,
            longitude = SEARCH_LONGITUDE,
            radiusKm = SEARCH_RADIUS_KM
        ).results.map { it.toDomain() }

    suspend fun getObservation(id: Long): Observation? =
        api.getObservation(id).results.firstOrNull()?.toDomain()

    companion object {
        const val SEARCH_LATITUDE = 50.4501
        const val SEARCH_LONGITUDE = 30.5234
        const val SEARCH_RADIUS_KM = 50
    }
}
