package com.example.citrusscan.domain.usecase

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.validation.Validators
import com.example.citrusscan.domain.model.ClassProbabilities
import com.example.citrusscan.domain.model.ClassifierName
import com.example.citrusscan.domain.model.ClassifierOutcome
import com.example.citrusscan.domain.model.ClassifierStatus
import com.example.citrusscan.domain.model.Label
import com.example.citrusscan.domain.model.Prediction
import com.example.citrusscan.domain.model.PredictionInput
import com.example.citrusscan.domain.repository.PredictionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PredictCitrusUseCaseTest {

    @Test
    fun returnsValidationErrorWhenWeightIsZero() = runTest {
        val useCase = PredictCitrusUseCase(
            repository = FakePredictionRepository(ApiResult.Success(samplePrediction())),
            validators = Validators(),
        )

        val result = useCase(sampleInput().copy(weight = 0f))

        assertTrue(result is ApiResult.Failure)
    }

    @Test
    fun sortsSuccessfulClassifiersByConfidenceDescending() = runTest {
        val useCase = PredictCitrusUseCase(
            repository = FakePredictionRepository(ApiResult.Success(samplePrediction())),
            validators = Validators(),
        )

        val result = useCase(sampleInput())

        assertTrue(result is ApiResult.Success)
        val prediction = (result as ApiResult.Success).data
        assertEquals(ClassifierName.SVM, prediction.results.first().classifier)
    }

    private class FakePredictionRepository(
        private val result: ApiResult<Prediction>,
    ) : PredictionRepository {
        override suspend fun predict(input: PredictionInput): ApiResult<Prediction> = result
    }

    private fun sampleInput(): PredictionInput = PredictionInput(
        imageBytes = byteArrayOf(1, 2, 3),
        imageFileName = "fruit.jpg",
        imageMimeType = "image/jpeg",
        weight = 120f,
        circumference = 24f,
    )

    private fun samplePrediction(): Prediction = Prediction(
        results = listOf(
            ClassifierOutcome(
                classifier = ClassifierName.BAYES,
                status = ClassifierStatus.OK,
                predictedLabel = Label.LIMON,
                confidence = 0.4,
                confidencePercent = 40.0,
                probabilities = ClassProbabilities(0.6, 0.4),
                detail = null,
            ),
            ClassifierOutcome(
                classifier = ClassifierName.SVM,
                status = ClassifierStatus.OK,
                predictedLabel = Label.NARANJA,
                confidence = 0.9,
                confidencePercent = 90.0,
                probabilities = ClassProbabilities(0.1, 0.9),
                detail = null,
            ),
        ),
        bestClassifier = ClassifierName.SVM,
        bestLabel = Label.NARANJA,
    )
}
