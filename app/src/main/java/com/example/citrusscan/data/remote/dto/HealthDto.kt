package com.example.citrusscan.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HealthDto(
    val status: String,
    val version: String? = null,
    @SerialName("uptime_seconds") val uptimeSeconds: Double? = null,
)
