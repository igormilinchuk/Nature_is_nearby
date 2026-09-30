package com.example.nature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nature.data.SampleObservations
import com.example.nature.ui.catalog.CatalogScreen
import com.example.nature.ui.theme.NatureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NatureTheme {
                CatalogScreen(observations = SampleObservations.items)
            }
        }
    }
}
