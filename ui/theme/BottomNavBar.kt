package com.example.uber.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.uber.navigation.Screen

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    onHomeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentScreen == Screen.HOME,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.HISTORY,
            onClick = onHistoryClick,
            icon = { Icon(Icons.Default.History, contentDescription = null) },
            label = { Text("History") }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.PROFILE,
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}