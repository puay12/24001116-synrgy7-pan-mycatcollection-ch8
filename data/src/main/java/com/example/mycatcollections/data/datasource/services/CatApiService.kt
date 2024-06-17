package com.example.mycatcollections.data.datasource.services

import com.example.mycatcollections.data.model.CatResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApiService {
    @GET("search")
    @Headers(
        "accept: application/json",
        "x-api-key: $API_KEY"
    )
    suspend fun getCatCollections(
        @Query("size") size: String = "med",
        @Query("mime_types") mimeTypes: String = "jpg",
        @Query("format") format: String = "json",
        @Query("has_breeds") hasBreeds: String = "true",
        @Query("order") order: String = "RANDOM",
        @Query("page") page: String = "0",
        @Query("limit") limit: String = "25"
    ) : List<CatResponse>?

    @GET("{id}")
    @Headers(
        "accept: application/json",
        "x-api-key: $API_KEY"
    )
    suspend fun getCatById(
        @Path("id") id: String
    ) : CatResponse?
}