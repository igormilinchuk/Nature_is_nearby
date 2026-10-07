package com.example.nature.ui.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nature.R
import com.example.nature.data.Observation
import com.example.nature.data.SampleObservations
import com.example.nature.ui.catalog.Badge
import com.example.nature.ui.catalog.formatLocation
import com.example.nature.ui.catalog.iconicTaxonLabel
import com.example.nature.ui.catalog.qualityLabel
import com.example.nature.ui.theme.NatureTheme

@Composable
fun ObservationDetailRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ObservationDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObservationDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObservationDetailScreen(
    uiState: ObservationDetailUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val observation = uiState.observation

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = observation?.taxon?.preferred_common_name
                            ?: stringResource(R.string.detail_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        if (observation == null) {
            NotFoundContent(
                observationId = uiState.observationId,
                onBackClick = onBackClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            ObservationDetailContent(
                observation = observation,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
private fun ObservationDetailContent(
    observation: Observation,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            Image(
                painter = painterResource(observation.photos.first().localRes),
                contentDescription = observation.taxon.preferred_common_name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Badge(
                text = stringResource(qualityLabel(observation.quality_grade)),
                containerColor = Color.Black.copy(alpha = 0.55f),
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = observation.taxon.preferred_common_name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = observation.taxon.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            DetailSection(title = R.string.detail_section_observation) {
                DetailRow(R.string.label_species_guess, observation.species_guess)
                DetailRow(R.string.label_date, observation.observed_on)
                DetailRow(R.string.label_place, observation.place_guess)
                DetailRow(R.string.label_location, formatLocation(observation.location))
                DetailRow(R.string.label_quality, stringResource(qualityLabel(observation.quality_grade)))
                DetailRow(R.string.label_observation_id, observation.id.toString())
            }

            DetailSection(title = R.string.detail_section_taxon) {
                DetailRow(R.string.label_taxon_group, stringResource(iconicTaxonLabel(observation.taxon.iconic_taxon_name)))
                DetailRow(R.string.label_rank, rankLabel(observation.taxon.rank))
                DetailRow(R.string.label_taxon_id, observation.taxon.id.toString())
            }

            observation.description?.let { description ->
                DetailSection(title = R.string.detail_section_description) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            val wikipediaUrl = observation.taxon.wikipedia_url
            if (observation.uri.isNotBlank() || wikipediaUrl != null) {
                DetailSection(title = R.string.detail_section_links) {
                    if (observation.uri.isNotBlank()) {
                        OutlinedButton(
                            onClick = { uriHandler.openUri(observation.uri) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.link_inaturalist))
                        }
                    }
                    if (wikipediaUrl != null) {
                        OutlinedButton(
                            onClick = { uriHandler.openUri(wikipediaUrl) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.link_wikipedia))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailSection(
    @StringRes title: Int,
    content: @Composable () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            HorizontalDivider()
            content()
        }
    }
}

@Composable
private fun DetailRow(
    @StringRes label: Int,
    value: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(140.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NotFoundContent(
    observationId: Long,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.detail_not_found_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.detail_not_found_message, observationId),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackClick) {
            Text(stringResource(R.string.detail_not_found_action))
        }
    }
}

private fun rankLabel(rank: String): String = when (rank) {
    "species" -> "вид"
    "subspecies" -> "підвид"
    "genus" -> "рід"
    else -> rank
}

@Preview(showBackground = true)
@Composable
private fun ObservationDetailScreenPreview() {
    val observation = SampleObservations.items[3]
    NatureTheme {
        ObservationDetailScreen(
            uiState = ObservationDetailUiState(observation.id, observation),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ObservationDetailNotFoundPreview() {
    NatureTheme {
        ObservationDetailScreen(
            uiState = ObservationDetailUiState(observationId = 42, observation = null),
            onBackClick = {}
        )
    }
}
