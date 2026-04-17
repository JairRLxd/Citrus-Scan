package com.example.citrusscan.core.config

import com.example.citrusscan.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

interface AppConfig {
    val apiBaseUrl: String
    val isDebug: Boolean
    val httpLoggingEnabled: Boolean
}

@Singleton
class AppConfigImpl @Inject constructor() : AppConfig {
    override val apiBaseUrl: String = BuildConfig.API_BASE_URL
    override val isDebug: Boolean = BuildConfig.DEBUG
    override val httpLoggingEnabled: Boolean = BuildConfig.ENABLE_HTTP_LOGGING
}
