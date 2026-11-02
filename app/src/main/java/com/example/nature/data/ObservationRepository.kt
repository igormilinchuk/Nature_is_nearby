package com.example.nature.data

import com.example.nature.data.remote.INaturalistApi
import com.example.nature.data.remote.NetworkModule
import com.example.nature.data.remote.toDomain
import retrofit2.HttpException

class ObservationRepository(
    private val api: INaturalistApi = NetworkModule.api
) {

    suspend fun getObservations(filter: TaxonFilter, area: SearchArea): List<Observation> =
        api.getObservations(
            iconicTaxa = filter.iconicTaxonName,
            latitude = area.latitude,
            longitude = area.longitude,
            radiusKm = SearchArea.RADIUS_KM
        ).results
            .map { it.toDomain() }
            .distinctBy { it.id }

    suspend fun getObservation(id: Long): Observation? =
        try {
            api.getObservation(id).results.firstOrNull()?.toDomain()
        } catch (e: HttpException) {
            if (e.code() == 404) null else throw e
        }
}
