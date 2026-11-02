package com.example.nature.data

data class SearchArea(
    val latitude: Double,
    val longitude: Double,
    val isUserLocation: Boolean
) {
    companion object {
        val Default = SearchArea(
            latitude = 50.4501,
            longitude = 30.5234,
            isUserLocation = false
        )
        const val RADIUS_KM = 50
    }
}
