package com.example.citrusscan.core.common

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorMessenger @Inject constructor() {

    fun toUserMessage(error: AppError): String = when (error) {
        is AppError.Network -> "No se pudo conectar con el servidor. Revisa tu conexión."
        is AppError.Timeout -> "La solicitud tardó demasiado. Intenta de nuevo."
        is AppError.Http -> when (error.code) {
            400 -> error.serverMessage ?: "Los datos enviados no son válidos."
            404 -> "Recurso no encontrado."
            413 -> "La imagen es demasiado grande."
            in 500..599 -> "Error del servidor. Intenta más tarde."
            else -> error.serverMessage ?: "Error HTTP ${error.code}."
        }
        is AppError.Serialization -> "La respuesta del servidor no tiene el formato esperado."
        is AppError.Validation -> error.message
        is AppError.Unknown -> error.message ?: "Ocurrió un error inesperado."
    }

    fun toKind(error: AppError): ErrorKind = when (error) {
        is AppError.Network, is AppError.Timeout -> ErrorKind.Network
        is AppError.Validation -> ErrorKind.Validation
        is AppError.Http -> ErrorKind.Server
        is AppError.Serialization, is AppError.Unknown -> ErrorKind.Unknown
    }
}

enum class ErrorKind { Network, Validation, Server, Unknown }
