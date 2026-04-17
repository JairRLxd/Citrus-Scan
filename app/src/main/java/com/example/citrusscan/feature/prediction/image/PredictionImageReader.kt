package com.example.citrusscan.feature.prediction.image

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.data.remote.image.ImageBytes

interface PredictionImageReader {
    suspend fun read(uri: String): ApiResult<ImageBytes>
}
