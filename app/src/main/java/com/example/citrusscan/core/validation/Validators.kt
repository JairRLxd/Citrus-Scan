package com.example.citrusscan.core.validation

import com.example.citrusscan.core.common.AppError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Validators @Inject constructor() {

    fun validateImage(bytes: ByteArray?): AppError.Validation? = when {
        bytes == null || bytes.isEmpty() ->
            AppError.Validation("image", "La imagen es obligatoria.")
        else -> null
    }

    fun validateWeight(value: Float): AppError.Validation? = when {
        value.isNaN() || value <= 0f ->
            AppError.Validation("weight", "El peso debe ser mayor que 0.")
        else -> null
    }

    fun validateCircumference(value: Float): AppError.Validation? = when {
        value.isNaN() || value <= 0f ->
            AppError.Validation("circumference", "La circunferencia debe ser mayor que 0.")
        else -> null
    }
}
