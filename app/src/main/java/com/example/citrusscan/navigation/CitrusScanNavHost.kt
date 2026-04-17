package com.example.citrusscan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.citrusscan.feature.diagnostics.DiagnosticsScreen
import com.example.citrusscan.feature.home.HomeScreen
import com.example.citrusscan.feature.prediction.ui.capture.CaptureScreen
import com.example.citrusscan.feature.prediction.ui.form.PredictionFormScreen
import com.example.citrusscan.feature.prediction.ui.result.PredictionResultScreen

@Composable
fun CitrusScanNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onStartAnalysis = { navController.navigate(Routes.CAPTURE) },
            )
        }

        composable(Routes.CAPTURE) {
            CaptureScreen(
                onBack = { navController.popBackStack() },
                onContinue = { selectedImageUri ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(Routes.IMAGE_URI_KEY, selectedImageUri)
                    navController.navigate(Routes.FORM)
                },
            )
        }

        composable(Routes.FORM) {
            val imageUri = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>(Routes.IMAGE_URI_KEY)

            PredictionFormScreen(
                initialImageUri = imageUri,
                onBack = { navController.popBackStack() },
                onShowResult = { payload ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(Routes.RESULT_PAYLOAD_KEY, payload)
                    navController.navigate(Routes.RESULT)
                },
            )
        }

        composable(Routes.RESULT) {
            val payload = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>(Routes.RESULT_PAYLOAD_KEY)

            PredictionResultScreen(
                payload = payload,
                onBack = { navController.popBackStack() },
                onRestart = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(Routes.DIAGNOSTICS) {
            DiagnosticsScreen(onBack = { navController.popBackStack() })
        }
    }
}
