package com.mikirinkode.flightsearchapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikirinkode.flightsearchapp.ui.screen.HomeScreen
import com.mikirinkode.flightsearchapp.ui.theme.FlightSearchAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flight Search") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) {
        HomeScreen(modifier = Modifier.padding(it))
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainAppPreview() {
    FlightSearchAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            MainApp()
        }
    }
}