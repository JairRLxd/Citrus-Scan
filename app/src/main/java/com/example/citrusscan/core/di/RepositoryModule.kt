package com.example.citrusscan.core.di

import com.example.citrusscan.data.remote.image.ImageReader
import com.example.citrusscan.data.remote.image.ImageReaderImpl
import com.example.citrusscan.data.repository.HealthRepositoryImpl
import com.example.citrusscan.data.repository.ModelsRepositoryImpl
import com.example.citrusscan.data.repository.PredictionRepositoryImpl
import com.example.citrusscan.data.repository.PreprocessingRepositoryImpl
import com.example.citrusscan.data.repository.TrainingRepositoryImpl
import com.example.citrusscan.domain.repository.HealthRepository
import com.example.citrusscan.domain.repository.ModelsRepository
import com.example.citrusscan.domain.repository.PredictionRepository
import com.example.citrusscan.domain.repository.PreprocessingRepository
import com.example.citrusscan.domain.repository.TrainingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPredictionRepository(impl: PredictionRepositoryImpl): PredictionRepository

    @Binds
    @Singleton
    abstract fun bindHealthRepository(impl: HealthRepositoryImpl): HealthRepository

    @Binds
    @Singleton
    abstract fun bindModelsRepository(impl: ModelsRepositoryImpl): ModelsRepository

    @Binds
    @Singleton
    abstract fun bindPreprocessingRepository(impl: PreprocessingRepositoryImpl): PreprocessingRepository

    @Binds
    @Singleton
    abstract fun bindTrainingRepository(impl: TrainingRepositoryImpl): TrainingRepository

    @Binds
    @Singleton
    abstract fun bindImageReader(impl: ImageReaderImpl): ImageReader
}
