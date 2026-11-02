package com.example.nature.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nature.R
import com.example.nature.data.LoadError
import com.example.nature.ui.theme.NatureTheme

@Composable
fun ErrorContent(
    error: LoadError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = when (error) {
        LoadError.NoNetwork -> stringResource(R.string.error_no_network_title)
        is LoadError.Server -> stringResource(R.string.error_server_title)
        LoadError.BadResponse -> stringResource(R.string.error_bad_response_title)
        LoadError.Unknown -> stringResource(R.string.error_unknown_title)
    }
    val message = when (error) {
        LoadError.NoNetwork -> stringResource(R.string.error_no_network_message)
        is LoadError.Server -> stringResource(R.string.error_server_message, error.code)
        LoadError.BadResponse -> stringResource(R.string.error_bad_response_message)
        LoadError.Unknown -> stringResource(R.string.error_unknown_message)
    }

    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.action_retry))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 320)
@Composable
private fun ErrorContentPreview() {
    NatureTheme {
        ErrorContent(error = LoadError.NoNetwork, onRetry = {})
    }
}
