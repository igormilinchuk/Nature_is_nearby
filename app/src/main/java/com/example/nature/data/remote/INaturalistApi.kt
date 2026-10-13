package com.example.nature.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface INaturalistApi {

    @GET("v1/observations")
    suspend fun getObservations(
        @Query("iconic_taxa") iconicTaxa: String?,
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radiusKm: Int,
        @Query("photos") withPhotos: Boolean = true,
        @Query("order_by") orderBy: String = "observed_on",
        @Query("per_page") perPage: Int = 30,
        @Query("locale") locale: String = "uk"
    ): ObservationsResponseDto

    @GET("v1/observations/{id}")
    suspend fun getObservation(
        @Path("id") id: Long,
        @Query("locale") locale: String = "uk"
    ): ObservationsResponseDto
}
