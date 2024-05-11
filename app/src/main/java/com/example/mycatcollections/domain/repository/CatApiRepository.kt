package com.example.mycatcollections.domain.repository

import com.example.mycatcollections.data.model.Cat

interface CatApiRepository {
    suspend fun getCatCollections() : List<Cat>
}