package com.example.citrusscan.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.citrusscan.ui.theme.CitrusNight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitrusTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = CitrusNight,
        titleContentColor = Color.White,
        navigationIconContentColor = Color.White,
        actionIconContentColor = Color.White,
    ),
    actions: @Composable () -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver")
                }
            }
        },
        actions = { actions() },
        colors = colors,
    )
}
