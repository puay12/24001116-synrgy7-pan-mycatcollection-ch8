package com.example.mycatcollections.data.repository

import com.example.mycatcollections.data.datasource.interfaces.CatRemoteDataSource
import com.example.mycatcollections.data.model.Cat
import com.example.mycatcollections.data.model.CatResponse
import com.example.mycatcollections.domain.repository.CatApiRepository

class CatApiRepositoryImpl(
    private val catRemoteDataSource: CatRemoteDataSource
) : CatApiRepository {
    override suspend fun getCatCollections(): List<Cat> {
        val response: List<CatResponse>? = catRemoteDataSource.fetchCatData()

        return response!!.map { it.toCat() }
    }

    override suspend fun getCatById(id: String): Cat {
        return catRemoteDataSource.fetchCatDataById(id)?.toCat()!!
    }

    private fun CatResponse.toCat() : Cat {
        return Cat(
            id = id!!,
            imgUrl = url!!,
            name = breeds?.get(0)?.name!!,
            weight = breeds.get(0)?.weight?.metric!!,
            lifeSpan = breeds.get(0)?.lifeSpan!!,
            temperament = breeds.get(0)?.temperament!!,
            origin = breeds.get(0)?.origin!!,
            description = breeds.get(0)?.description!!
        )
    }
}