package com.example.mycatcollections.domain.repository

import com.example.mycatcollections.domain.model.Cat

interface CatApiRepository {
    suspend fun getCatCollections() : List<Cat>
    suspend fun getCatById(id: String) : Cat
}