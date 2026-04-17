package com.example.citrusscan.data.remote.datasource

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.IoDispatcher
import com.example.citrusscan.core.network.ApiErrorParser
import com.example.citrusscan.core.network.safeApiCall
import com.example.citrusscan.data.remote.api.CitrusScanApi
import com.example.citrusscan.data.remote.dto.ModelsDto
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ModelsRemoteDataSource @Inject constructor(
    private val api: CitrusScanApi,
    private val errorParser: ApiErrorParser,
    @IoDispatcher private val io: CoroutineDispatcher,
) {
    suspend fun getModels(): ApiResult<ModelsDto> = withContext(io) {
        safeApiCall(errorParser) { api.models() }
    }
}
