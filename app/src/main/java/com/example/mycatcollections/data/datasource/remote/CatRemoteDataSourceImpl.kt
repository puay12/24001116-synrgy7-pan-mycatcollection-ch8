package com.example.mycatcollections.data.datasource.remote

import com.example.mycatcollections.data.datasource.interfaces.CatRemoteDataSource
import com.example.mycatcollections.data.datasource.services.CatApiService
import com.example.mycatcollections.data.model.CatResponse

class CatRemoteDataSourceImpl(
    private val catApiService: CatApiService
) : CatRemoteDataSource {
    override suspend fun fetchCatData(): List<CatResponse> {
        return catApiService.getCatCollections().results
    }
}