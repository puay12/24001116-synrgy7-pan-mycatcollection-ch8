package com.example.mycatcollections.data.datasource.interfaces

import com.example.mycatcollections.data.model.CatResponse

interface CatRemoteDataSource {
    suspend fun fetchCatData(): List<CatResponse>
}