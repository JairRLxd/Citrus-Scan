package com.example.citrusscan.domain.model

enum class ClassifierName(val apiKey: String, val displayName: String) {
    SVM("svm", "SVM"),
    BAYES("bayes", "Bayes"),
    PERCEPTRON("perceptron", "Perceptrón"),
    UNKNOWN("", "Desconocido");

    companion object {
        fun fromApi(value: String?): ClassifierName =
            entries.firstOrNull { it.apiKey.equals(value, ignoreCase = true) } ?: UNKNOWN
    }
}

enum class Label(val apiKey: String, val displayName: String) {
    LIMON("limon", "Limón"),
    NARANJA("naranja", "Naranja"),
    UNKNOWN("", "Desconocido");

    companion object {
        fun fromApi(value: String?): Label? = when {
            value == null -> null
            else -> entries.firstOrNull { it.apiKey.equals(value, ignoreCase = true) } ?: UNKNOWN
        }
    }
}

enum class ClassifierStatus(val apiKey: String) {
    OK("ok"),
    MODEL_NOT_TRAINED("model_not_trained"),
    ERROR("error"),
    UNKNOWN("");

    companion object {
        fun fromApi(value: String?): ClassifierStatus =
            entries.firstOrNull { it.apiKey.equals(value, ignoreCase = true) } ?: UNKNOWN
    }
}

data class ClassProbabilities(
    val limon: Double?,
    val naranja: Double?,
)

data class ClassifierOutcome(
    val classifier: ClassifierName,
    val status: ClassifierStatus,
    val predictedLabel: Label?,
    val confidence: Double?,
    val confidencePercent: Double?,
    val probabilities: ClassProbabilities?,
    val detail: String?,
)

data class Prediction(
    val results: List<ClassifierOutcome>,
    val bestClassifier: ClassifierName?,
    val bestLabel: Label?,
)
