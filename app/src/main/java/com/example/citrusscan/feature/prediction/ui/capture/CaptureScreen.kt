package com.example.citrusscan.feature.prediction.ui.capture

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.citrusscan.feature.prediction.state.CaptureUiEvent
import com.example.citrusscan.ui.theme.CitrusPeel
import com.example.citrusscan.ui.theme.CitrusText
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
    var cameraError by remember { mutableStateOf<String?>(null) }

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
            cameraError = null
        } else {
            cameraError = "No se pudo capturar la foto."
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            launchCameraCapture(
                context = context,
                cameraLauncher = cameraLauncher,
                onUriReady = { pendingCameraUri = it },
                onError = { cameraError = it },
            )
        } else {
            cameraError = "Permiso de camara denegado."
        }
    }

    Scaffold(
        containerColor = Color(0xFF101014),
        topBar = {
            TopAppBar(
                title = { Text("Captura") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF101014),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .navigationBarsPadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = CitrusPeel.copy(alpha = 0.35f),
            ) {
                Text(
                    text = "Elige una imagen desde galeria o toma una foto para iniciar el flujo de prediccion.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.padding(14.dp),
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(24.dp),
            ) {
                Crossfade(targetState = state.selectedImageUri, label = "capture_preview") { selectedImageUri ->
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Vista previa",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Aun no hay una imagen seleccionada.",
                                style = MaterialTheme.typography.titleMedium,
                                color = CitrusText.copy(alpha = 0.72f),
                            )
                        }
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
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(Icons.Rounded.PhotoLibrary, contentDescription = null)
                    Text("Galeria")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        cameraError = null
                        val permissionGranted = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA,
                        ) == PackageManager.PERMISSION_GRANTED

                        if (permissionGranted) {
                            launchCameraCapture(
                                context = context,
                                cameraLauncher = cameraLauncher,
                                onUriReady = { pendingCameraUri = it },
                                onError = { cameraError = it },
                            )
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null)
                    Text("Camara")
                }
            }

            AnimatedVisibility(
                visible = cameraError != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                cameraError?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
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
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(Icons.Rounded.Delete, contentDescription = null)
                    Text("Limpiar")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    enabled = state.canContinue,
                    onClick = { state.selectedImageUri?.let(onContinue) },
                    shape = RoundedCornerShape(14.dp),
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

private fun launchCameraCapture(
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    onUriReady: (Uri) -> Unit,
    onError: (String) -> Unit,
) {
    val hasCameraApp = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        .resolveActivity(context.packageManager) != null
    if (!hasCameraApp) {
        onError("No hay una app de camara disponible.")
        return
    }

    runCatching {
        createCameraImageUri(context)
    }.onSuccess { uri ->
        onUriReady(uri)
        cameraLauncher.launch(uri)
    }.onFailure {
        onError("No se pudo preparar la imagen para la camara.")
    }
}
