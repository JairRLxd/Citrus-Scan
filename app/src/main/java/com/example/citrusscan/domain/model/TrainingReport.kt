package com.example.citrusscan.domain.model

enum class TrainingStatus(val apiKey: String) {
    OK("ok"),
    ERROR("error"),
    SKIPPED("skipped"),
    UNKNOWN("");

    companion object {
        fun fromApi(value: String?): TrainingStatus =
            entries.firstOrNull { it.apiKey.equals(value, ignoreCase = true) } ?: UNKNOWN
    }
}

data class TrainedClassifier(
    val classifier: ClassifierName,
    val accuracy: Double?,
    val status: TrainingStatus,
)

data class TrainingReport(
    val success: Boolean,
    val items: List<TrainedClassifier>,
    val detail: String?,
)

data class TrainingInput(
    val datasetPath: String? = null,
    val force: Boolean = false,
)
