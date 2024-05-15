package com.example.mycatcollections.data.datasource.interfaces

interface AuthLocalDataSource {
    suspend fun userLogin(username: String, password: String) : String
    suspend fun setToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}