package com.example.citrusscan.feature.prediction.ui.capture

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.citrusscan.feature.prediction.state.CaptureUiEvent
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureScreen(
    onBack: () -> Unit,
    onContinue: (String) -> Unit,
    viewModel: CaptureViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle().value
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        uri?.let { viewModel.onEvent(CaptureUiEvent.ImageSelected(it.toString())) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        if (success) {
            pendingCameraUri?.let {
                viewModel.onEvent(CaptureUiEvent.ImageSelected(it.toString()))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Captura") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Elige una imagen desde galeria o toma una foto para iniciar el flujo de prediccion.",
                style = MaterialTheme.typography.bodyLarge,
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                shape = RoundedCornerShape(28.dp),
            ) {
                if (state.selectedImageUri != null) {
                    AsyncImage(
                        model = state.selectedImageUri,
                        contentDescription = "Vista previa",
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Aun no hay una imagen seleccionada.",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    },
                ) {
                    Icon(Icons.Rounded.PhotoLibrary, contentDescription = null)
                    Text("Galeria")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val output = createCameraImageUri(context)
                        pendingCameraUri = output
                        cameraLauncher.launch(output)
                    },
                ) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null)
                    Text("Camara")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = state.selectedImageUri != null,
                    onClick = { viewModel.onEvent(CaptureUiEvent.ClearSelection) },
                ) {
                    Icon(Icons.Rounded.Delete, contentDescription = null)
                    Text("Limpiar")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    enabled = state.canContinue,
                    onClick = { state.selectedImageUri?.let(onContinue) },
                ) {
                    Text("Continuar")
                }
            }
        }
    }
}

private fun createCameraImageUri(context: Context): Uri {
    val imagesDir = File(context.cacheDir, "images").apply {
        if (!exists()) mkdirs()
    }
    val file = File.createTempFile("citrus_capture_", ".jpg", imagesDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file,
    )
}
