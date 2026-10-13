package com.example.nature.ui.catalog

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nature.R
import com.example.nature.data.Observation
import com.example.nature.data.commonName
import com.example.nature.data.scientificName
import com.example.nature.ui.previewObservation
import com.example.nature.ui.theme.NatureTheme

@Composable
fun ObservationCard(
    observation: Observation,
    showScientificNames: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        val commonName = observation.commonName ?: stringResource(R.string.unknown_species)
        val scientificName = observation.scientificName
        val title = if (showScientificNames) scientificName ?: commonName else commonName
        val subtitle = if (showScientificNames) commonName.takeIf { scientificName != null } else scientificName
        Column {
            ObservationImage(observation)
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = if (showScientificNames && scientificName != null) FontStyle.Italic else FontStyle.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = if (showScientificNames) FontStyle.Normal else FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                InfoItem(
                    label = R.string.label_place,
                    value = observation.placeGuess ?: stringResource(R.string.value_unknown)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoItem(
                        label = R.string.label_date,
                        value = observation.observedOn ?: stringResource(R.string.value_unknown),
                        modifier = Modifier.weight(1f)
                    )
                    InfoItem(
                        label = R.string.label_location,
                        value = formatLocation(observation.location) ?: stringResource(R.string.value_unknown),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ObservationImage(observation: Observation) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text(
            text = stringResource(R.string.photo_placeholder),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.Center)
        )
        Badge(
            text = stringResource(iconicTaxonLabel(observation.taxon?.iconicTaxonName)),
            containerColor = Color.Black.copy(alpha = 0.55f),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        )
        Badge(
            text = stringResource(qualityLabel(observation.qualityGrade)),
            containerColor = if (observation.qualityGrade == "research") {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.tertiaryContainer
            },
            contentColor = if (observation.qualityGrade == "research") {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onTertiaryContainer
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        )
    }
}

@Composable
internal fun Badge(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = containerColor,
        contentColor = contentColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun InfoItem(
    @StringRes label: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

internal fun formatLocation(location: String?): String? =
    location?.split(",")
        ?.mapNotNull { it.trim().toDoubleOrNull() }
        ?.takeIf { it.size == 2 }
        ?.joinToString(", ") { "%.4f".format(java.util.Locale.US, it) }

@StringRes
internal fun iconicTaxonLabel(iconicTaxonName: String?): Int = when (iconicTaxonName) {
    "Mammalia" -> R.string.taxon_mammalia
    "Aves" -> R.string.taxon_aves
    "Reptilia" -> R.string.taxon_reptilia
    "Amphibia" -> R.string.taxon_amphibia
    "Insecta" -> R.string.taxon_insecta
    "Plantae" -> R.string.taxon_plantae
    "Fungi" -> R.string.taxon_fungi
    else -> R.string.taxon_other
}

@StringRes
internal fun qualityLabel(qualityGrade: String): Int = when (qualityGrade) {
    "research" -> R.string.quality_research
    "needs_id" -> R.string.quality_needs_id
    else -> R.string.quality_casual
}

@Preview(showBackground = true)
@Composable
private fun ObservationCardPreview() {
    NatureTheme {
        ObservationCard(
            observation = previewObservation,
            showScientificNames = false,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
