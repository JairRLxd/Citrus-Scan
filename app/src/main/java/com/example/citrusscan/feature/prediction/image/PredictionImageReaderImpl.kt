package com.example.citrusscan.feature.prediction.image

import android.net.Uri
import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.data.remote.image.ImageBytes
import com.example.citrusscan.data.remote.image.ImageReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PredictionImageReaderImpl @Inject constructor(
    private val imageReader: ImageReader,
) : PredictionImageReader {

    override suspend fun read(uri: String): ApiResult<ImageBytes> = imageReader.read(Uri.parse(uri))
}
