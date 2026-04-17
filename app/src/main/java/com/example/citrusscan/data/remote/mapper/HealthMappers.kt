package com.example.citrusscan.data.remote.mapper

import com.example.citrusscan.data.remote.dto.HealthDto
import com.example.citrusscan.domain.model.Health

fun HealthDto.toDomain(): Health = Health(
    isUp = status.equals("ok", ignoreCase = true),
    version = version,
    uptimeSeconds = uptimeSeconds,
)
