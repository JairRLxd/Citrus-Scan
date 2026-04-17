package com.example.citrusscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.citrusscan.navigation.CitrusScanNavHost
import com.example.citrusscan.ui.theme.CitrusScanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CitrusScanTheme {
                CitrusScanNavHost()
            }
        }
    }
}
