package com.example.citrusscan.domain.model

data class PredictionInput(
    val imageBytes: ByteArray,
    val imageFileName: String,
    val imageMimeType: String,
    val weight: Float,
    val circumference: Float,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PredictionInput
        if (!imageBytes.contentEquals(other.imageBytes)) return false
        if (imageFileName != other.imageFileName) return false
        if (imageMimeType != other.imageMimeType) return false
        if (weight != other.weight) return false
        if (circumference != other.circumference) return false
        return true
    }

    override fun hashCode(): Int {
        var result = imageBytes.contentHashCode()
        result = 31 * result + imageFileName.hashCode()
        result = 31 * result + imageMimeType.hashCode()
        result = 31 * result + weight.hashCode()
        result = 31 * result + circumference.hashCode()
        return result
    }
}
