package com.example.citrusscan.domain.model

data class Health(
    val isUp: Boolean,
    val version: String?,
    val uptimeSeconds: Double?,
)
