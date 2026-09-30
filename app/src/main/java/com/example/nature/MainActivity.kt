package com.example.nature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nature.ui.theme.NatureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NatureTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BriefScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BriefScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.brief_tagline),
            style = MaterialTheme.typography.bodyLarge
        )
        BriefSection(
            title = R.string.brief_screens_title,
            items = listOf(
                R.string.brief_screen_catalog,
                R.string.brief_screen_details,
                R.string.brief_screen_list,
                R.string.brief_screen_settings
            )
        )
        BriefSection(
            title = R.string.brief_source_title,
            items = listOf(
                R.string.brief_source_api,
                R.string.brief_source_request,
                R.string.brief_source_image
            ),
            monospaceIndex = 1
        )
        BriefSection(
            title = R.string.brief_local_title,
            items = listOf(R.string.brief_local_text)
        )
    }
}

@Composable
fun BriefSection(
    @StringRes title: Int,
    items: List<Int>,
    monospaceIndex: Int = -1
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
            items.forEachIndexed { index, item ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "•", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = stringResource(item),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = if (index == monospaceIndex) FontFamily.Monospace else null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BriefScreenPreview() {
    NatureTheme {
        BriefScreen()
    }
}
