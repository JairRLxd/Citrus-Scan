package com.example.citrusscan.core.di

import com.example.citrusscan.feature.prediction.image.PredictionImageReader
import com.example.citrusscan.feature.prediction.image.PredictionImageReaderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PresentationModule {

    @Binds
    @Singleton
    abstract fun bindPredictionImageReader(
        impl: PredictionImageReaderImpl,
    ): PredictionImageReader
}
