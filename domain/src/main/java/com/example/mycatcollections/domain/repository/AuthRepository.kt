package com.example.mycatcollections.domain.repository

interface AuthRepository {
    suspend fun userLogin(username: String, password: String): String
    suspend fun setToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}