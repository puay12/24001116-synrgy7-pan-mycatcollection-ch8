package com.example.mycatcollections.data.repository

import com.example.mycatcollections.data.datasource.interfaces.AuthLocalDataSource
import com.example.mycatcollections.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authLocalDataSource: AuthLocalDataSource
) : AuthRepository {
    override suspend fun userLogin(username: String, password: String): String {
        return authLocalDataSource.userLogin(username, password)
    }

    override suspend fun setToken(token: String) {
        authLocalDataSource.setToken(token)
    }

    override suspend fun getToken(): String? {
        return authLocalDataSource.getToken()
    }

    override suspend fun clearToken() {
        authLocalDataSource.clearToken()
    }

}