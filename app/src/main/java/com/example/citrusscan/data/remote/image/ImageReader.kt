package com.example.citrusscan.data.remote.image

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.AppError
import com.example.citrusscan.core.common.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

data class ImageBytes(
    val bytes: ByteArray,
    val fileName: String,
    val mimeType: String,
)

interface ImageReader {
    suspend fun read(uri: Uri): ApiResult<ImageBytes>
}

@Singleton
class ImageReaderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val io: CoroutineDispatcher,
) : ImageReader {

    override suspend fun read(uri: Uri): ApiResult<ImageBytes> = withContext(io) {
        runCatching {
            val resolver = context.contentResolver
            val mime = resolver.getType(uri) ?: guessMimeFromUri(uri) ?: "image/jpeg"
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mime) ?: "jpg"
            val fileName = "citrus_${System.currentTimeMillis()}.$extension"

            val bytes = resolver.openInputStream(uri)?.use { it.readBytes() }
                ?: error("No se pudo abrir la imagen seleccionada.")

            ImageBytes(bytes = bytes, fileName = fileName, mimeType = mime)
        }.fold(
            onSuccess = { ApiResult.Success(it) },
            onFailure = { ApiResult.Failure(AppError.Unknown(it.message, it)) },
        )
    }

    private fun guessMimeFromUri(uri: Uri): String? {
        val ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString()) ?: return null
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.lowercase())
    }
}
