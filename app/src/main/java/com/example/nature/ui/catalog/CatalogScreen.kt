package com.example.nature.ui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nature.R
import com.example.nature.data.Observation
import com.example.nature.data.SampleObservations
import com.example.nature.data.TaxonFilter
import com.example.nature.data.filterBy
import com.example.nature.ui.theme.NatureTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    observations: List<Observation>,
    modifier: Modifier = Modifier
) {
    var selectedFilter by rememberSaveable { mutableStateOf(TaxonFilter.ALL) }
    val visible = remember(observations, selectedFilter) { observations.filterBy(selectedFilter) }

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
                                visible.size,
                                visible.size
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                options = TaxonFilter.entries,
                selected = selectedFilter,
                onSelect = { selectedFilter = it }
            )

            if (visible.isEmpty()) {
                EmptyState(
                    filter = selectedFilter,
                    onShowAll = { selectedFilter = TaxonFilter.ALL },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.catalog_header),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    items(visible, key = { it.id }) { observation ->
                        ObservationCard(observation = observation)
                    }
                }
            }
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
        items(options) { option ->
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

@Preview(showBackground = true)
@Composable
private fun CatalogScreenPreview() {
    NatureTheme {
        CatalogScreen(observations = SampleObservations.items)
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
