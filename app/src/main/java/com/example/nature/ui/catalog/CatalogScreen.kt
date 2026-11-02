package com.example.nature.ui.catalog

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nature.R
import com.example.nature.ui.previewObservation
import com.example.nature.data.Observation
import com.example.nature.data.SearchArea
import com.example.nature.data.TaxonFilter
import com.example.nature.ui.common.ErrorContent
import com.example.nature.ui.theme.NatureTheme

@Composable
fun CatalogRoute(
    onItemClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CatalogViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onLocationPermissionResult(granted)
    }
    CatalogScreen(
        uiState = uiState,
        onLocationClick = {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        },
        onFilterSelected = viewModel::onFilterSelected,
        onShowAll = viewModel::onShowAllClicked,
        onRetry = viewModel::onRetryClicked,
        onItemClick = onItemClick,
        onSettingsClick = onSettingsClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    uiState: CatalogUiState,
    onLocationClick: () -> Unit,
    onFilterSelected: (TaxonFilter) -> Unit,
    onShowAll: () -> Unit,
    onRetry: () -> Unit,
    onItemClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val count = (uiState.content as? CatalogContent.Success)?.observations?.size ?: 0
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = pluralStringResource(
                                R.plurals.observations_count,
                                count,
                                count
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    if (uiState.isLocating) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(12.dp)
                                .size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(onClick = onLocationClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_my_location),
                                contentDescription = stringResource(R.string.action_search_near_me)
                            )
                        }
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_settings),
                            contentDescription = stringResource(R.string.settings_title)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TaxonFilterRow(
                options = uiState.filters,
                selected = uiState.selectedFilter,
                onSelect = onFilterSelected
            )
            SearchAreaInfo(
                isUserLocation = uiState.searchArea.isUserLocation,
                message = uiState.locationMessage
            )

            when (val content = uiState.content) {
                CatalogContent.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                is CatalogContent.Error -> ErrorContent(
                    error = content.error,
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize()
                )
                CatalogContent.Empty -> EmptyState(
                    filter = uiState.selectedFilter,
                    onShowAll = onShowAll,
                    modifier = Modifier.fillMaxSize()
                )
                is CatalogContent.Success -> ObservationList(
                    observations = content.observations,
                    showScientificNames = uiState.showScientificNames,
                    onItemClick = onItemClick,
                    listState = listState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun ObservationList(
    observations: List<Observation>,
    showScientificNames: Boolean,
    onItemClick: (Long) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "header", contentType = "header") {
            Text(
                text = stringResource(R.string.catalog_header),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        items(
            items = observations,
            key = { observation -> observation.id },
            contentType = { "observation" }
        ) { observation ->
            ObservationCard(
                observation = observation,
                showScientificNames = showScientificNames,
                onClick = { onItemClick(observation.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxonFilterRow(
    options: List<TaxonFilter>,
    selected: TaxonFilter,
    onSelect: (TaxonFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(options, key = { option -> option.name }) { option ->
            FilterChip(
                selected = option == selected,
                onClick = { onSelect(option) },
                label = { Text(stringResource(option.label)) }
            )
        }
    }
}

@Composable
private fun EmptyState(
    filter: TaxonFilter,
    onShowAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.empty_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.empty_message, stringResource(filter.label)),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onShowAll) {
            Text(stringResource(R.string.empty_show_all))
        }
    }
}

@Composable
private fun SearchAreaInfo(
    isUserLocation: Boolean,
    message: LocationMessage?
) {
    val text = when (message) {
        LocationMessage.PERMISSION_DENIED -> stringResource(R.string.location_permission_denied, SearchArea.RADIUS_KM)
        LocationMessage.UNAVAILABLE -> stringResource(R.string.location_unavailable, SearchArea.RADIUS_KM)
        null -> if (isUserLocation) {
            stringResource(R.string.search_area_user, SearchArea.RADIUS_KM)
        } else {
            stringResource(R.string.search_area_default, SearchArea.RADIUS_KM)
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = if (message != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun CatalogScreenPreview() {
    NatureTheme {
        CatalogScreen(
            uiState = CatalogUiState(content = CatalogContent.Success(listOf(previewObservation))),
            onLocationClick = {},
            onFilterSelected = {},
            onShowAll = {},
            onRetry = {},
            onItemClick = {},
            onSettingsClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TaxonFilterRowPreview() {
    NatureTheme {
        TaxonFilterRow(
            options = TaxonFilter.entries,
            selected = TaxonFilter.AVES,
            onSelect = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 320)
@Composable
private fun EmptyStatePreview() {
    NatureTheme {
        EmptyState(filter = TaxonFilter.FUNGI, onShowAll = {})
    }
}
