package com.example.citrusscan.feature.prediction

import app.cash.turbine.test
import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.ErrorMessenger
import com.example.citrusscan.data.remote.image.ImageBytes
import com.example.citrusscan.domain.model.ClassProbabilities
import com.example.citrusscan.domain.model.ClassifierName
import com.example.citrusscan.domain.model.ClassifierOutcome
import com.example.citrusscan.domain.model.ClassifierStatus
import com.example.citrusscan.domain.model.Label
import com.example.citrusscan.domain.model.Prediction
import com.example.citrusscan.domain.model.PredictionInput
import com.example.citrusscan.domain.usecase.PredictCitrusUseCase
import com.example.citrusscan.domain.repository.PredictionRepository
import com.example.citrusscan.core.validation.Validators
import com.example.citrusscan.feature.prediction.image.PredictionImageReader
import com.example.citrusscan.feature.prediction.state.PredictionFormEffect
import com.example.citrusscan.feature.prediction.state.PredictionUiEvent
import com.example.citrusscan.feature.prediction.ui.form.PredictionFormViewModel
import com.example.citrusscan.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PredictionFormViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun submitEmitsNavigationEffectOnSuccess() = runTest {
        val useCase = PredictCitrusUseCase(
            repository = FakePredictionRepository(ApiResult.Success(samplePrediction())),
            validators = Validators(),
        )
        val imageReader = object : PredictionImageReader {
            override suspend fun read(uri: String): ApiResult<ImageBytes> = ApiResult.Success(
                ImageBytes(
                    bytes = byteArrayOf(1, 2, 3),
                    fileName = "fruit.jpg",
                    mimeType = "image/jpeg",
                ),
            )
        }
        val viewModel = PredictionFormViewModel(
            predictCitrusUseCase = useCase,
            imageReader = imageReader,
            errorMessenger = ErrorMessenger(),
        )

        viewModel.onEvent(PredictionUiEvent.ImagePicked("content://fruit"))
        viewModel.onEvent(PredictionUiEvent.WeightChanged("150"))
        viewModel.onEvent(PredictionUiEvent.CircumferenceChanged("25"))

        viewModel.effects.test {
            viewModel.onEvent(PredictionUiEvent.Submit)

            val effect = awaitItem()
            assertTrue(effect is PredictionFormEffect.NavigateToResult)
            assertFalse(viewModel.state.value.isSubmitting)
        }
    }

    private class FakePredictionRepository(
        private val result: ApiResult<Prediction>,
    ) : PredictionRepository {
        override suspend fun predict(input: PredictionInput): ApiResult<Prediction> = result
    }

    private fun samplePrediction(): Prediction = Prediction(
        results = listOf(
            ClassifierOutcome(
                classifier = ClassifierName.SVM,
                status = ClassifierStatus.OK,
                predictedLabel = Label.NARANJA,
                confidence = 0.88,
                confidencePercent = 88.0,
                probabilities = ClassProbabilities(limon = 0.12, naranja = 0.88),
                detail = null,
            ),
        ),
        bestClassifier = ClassifierName.SVM,
        bestLabel = Label.NARANJA,
    )
}
