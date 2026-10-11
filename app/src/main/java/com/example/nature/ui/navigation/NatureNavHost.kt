package com.example.nature.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nature.ui.catalog.CatalogRoute
import com.example.nature.ui.detail.ObservationDetailRoute
import com.example.nature.ui.settings.SettingsRoute

object NatureDestinations {
    const val CATALOG = "catalog"
    const val SETTINGS = "settings"
    const val OBSERVATION_ID = "observationId"
    const val DETAIL = "observation/{$OBSERVATION_ID}"

    fun detail(observationId: Long): String = "observation/$observationId"
}

@Composable
fun NatureNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NatureDestinations.CATALOG
    ) {
        composable(NatureDestinations.CATALOG) {
            CatalogRoute(
                onItemClick = { id -> navController.navigate(NatureDestinations.detail(id)) },
                onSettingsClick = { navController.navigate(NatureDestinations.SETTINGS) }
            )
        }
        composable(NatureDestinations.SETTINGS) {
            SettingsRoute(
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(
            route = NatureDestinations.DETAIL,
            arguments = listOf(
                navArgument(NatureDestinations.OBSERVATION_ID) { type = NavType.LongType }
            )
        ) {
            ObservationDetailRoute(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
