package com.example.nature.data

import com.example.nature.data.remote.INaturalistApi
import com.example.nature.data.remote.NetworkModule
import com.example.nature.data.remote.toDomain
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import retrofit2.HttpException

class ObservationRepository(
    private val api: INaturalistApi = NetworkModule.api
) {

    suspend fun getObservations(filter: TaxonFilter, area: SearchArea): List<Observation> =
        requestLimiter.withPermit {
            api.getObservations(
                iconicTaxa = filter.iconicTaxonName,
                latitude = area.latitude,
                longitude = area.longitude,
                radiusKm = SearchArea.RADIUS_KM
            )
        }.results
            .map { it.toDomain() }
            .distinctBy { it.id }

    suspend fun getCounts(filters: List<TaxonFilter>, area: SearchArea): Map<TaxonFilter, Int> =
        coroutineScope {
            filters.map { filter ->
                async { filter to getCount(filter, area) }
            }.awaitAll()
                .mapNotNull { (filter, count) -> count?.let { filter to it } }
                .toMap()
        }

    private suspend fun getCount(filter: TaxonFilter, area: SearchArea): Int? =
        try {
            requestLimiter.withPermit {
                api.getObservations(
                    iconicTaxa = filter.iconicTaxonName,
                    latitude = area.latitude,
                    longitude = area.longitude,
                    radiusKm = SearchArea.RADIUS_KM,
                    perPage = 1
                ).totalResults
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }

    suspend fun getObservation(id: Long): Observation? =
        try {
            requestLimiter.withPermit { api.getObservation(id) }.results.firstOrNull()?.toDomain()
        } catch (e: HttpException) {
            if (e.code() == 404) null else throw e
        }

    companion object {
        const val MAX_PARALLEL_REQUESTS = 3
        private val requestLimiter = Semaphore(MAX_PARALLEL_REQUESTS)
    }
}
