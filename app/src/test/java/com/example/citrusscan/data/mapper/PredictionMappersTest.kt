package com.example.citrusscan.data.mapper

import com.example.citrusscan.data.remote.dto.ClassProbabilitiesDto
import com.example.citrusscan.data.remote.dto.ClassifierResultDto
import com.example.citrusscan.data.remote.dto.PredictResponseDto
import com.example.citrusscan.data.remote.mapper.toDomain
import com.example.citrusscan.domain.model.ClassifierName
import com.example.citrusscan.domain.model.ClassifierStatus
import com.example.citrusscan.domain.model.Label
import org.junit.Assert.assertEquals
import org.junit.Test

class PredictionMappersTest {

    @Test
    fun mapsPredictResponseIntoDomainModel() {
        val dto = PredictResponseDto(
            results = listOf(
                ClassifierResultDto(
                    classifier = "svm",
                    status = "ok",
                    predictedLabel = "naranja",
                    confidence = 0.92,
                    confidencePercent = 92.0,
                    classProbabilities = ClassProbabilitiesDto(limon = 0.08, naranja = 0.92),
                ),
                ClassifierResultDto(
                    classifier = "bayes",
                    status = "model_not_trained",
                    detail = "train first",
                ),
            ),
            bestClassifier = "svm",
            bestLabel = "naranja",
        )

        val domain = dto.toDomain()

        assertEquals(ClassifierName.SVM, domain.bestClassifier)
        assertEquals(Label.NARANJA, domain.bestLabel)
        assertEquals(ClassifierStatus.MODEL_NOT_TRAINED, domain.results[1].status)
        assertEquals(92.0, domain.results.first().confidencePercent ?: 0.0, 0.0)
    }
}
