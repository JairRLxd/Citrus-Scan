package com.example.citrusscan.data.remote.api

import com.example.citrusscan.data.remote.dto.HealthDto
import com.example.citrusscan.data.remote.dto.ModelsDto
import com.example.citrusscan.data.remote.dto.PredictResponseDto
import com.example.citrusscan.data.remote.dto.PreprocessingRecommendationDto
import com.example.citrusscan.data.remote.dto.PreprocessingRecommendationRequestDto
import com.example.citrusscan.data.remote.dto.PreprocessingStatusDto
import com.example.citrusscan.data.remote.dto.TrainRequestDto
import com.example.citrusscan.data.remote.dto.TrainResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CitrusScanApi {

    @GET("v1/health")
    suspend fun health(): HealthDto

    @GET("v1/models")
    suspend fun models(): ModelsDto

    @GET("v1/preprocessing/status")
    suspend fun preprocessingStatus(): PreprocessingStatusDto

    @POST("v1/preprocessing/recommendation")
    suspend fun preprocessingRecommendation(
        @Body body: PreprocessingRecommendationRequestDto,
    ): PreprocessingRecommendationDto

    @POST("v1/train")
    suspend fun train(@Body body: TrainRequestDto): TrainResponseDto

    @Multipart
    @POST("v1/predict")
    suspend fun predict(
        @Part file: MultipartBody.Part,
        @Part("weight") weight: RequestBody,
        @Part("circumference") circumference: RequestBody,
    ): PredictResponseDto
}
