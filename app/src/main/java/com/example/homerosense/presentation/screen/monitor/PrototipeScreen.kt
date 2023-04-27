package com.example.homerosense.presentation.screen.monitor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



@Composable
fun TabLayout() {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                when (selectedTabIndex) {
                    0 -> TabContent("This is the content for Home tab")
                    1 -> TabContent("This is the content for Profile tab")
                    2 -> TabContent("This is the content for Settings tab")
                    3 -> TabContent("This is the content for Favorites tab")
                }
            }
        }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.Gray,
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                text = { Text("Home") }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                text = { Text("Profile") }
            )
            Tab(
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 },
                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                text = { Text("Settings") }
            )
            Tab(
                selected = selectedTabIndex == 3,
                onClick = { selectedTabIndex = 3 },
                icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                text = { Text("Favorites") }
            )
        }
    }
}

@Composable
fun TabContent(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}