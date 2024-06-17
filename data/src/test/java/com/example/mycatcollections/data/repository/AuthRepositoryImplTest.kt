package com.example.mycatcollections.data.repository

import com.example.mycatcollections.data.datasource.interfaces.AuthLocalDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AuthRepositoryImplTest {
    private val dataSource = mock<AuthLocalDataSource>()
    private val authRepository = AuthRepositoryImpl(
        authLocalDataSource = dataSource
    )

    @Test
    fun userLogin() = runTest {
        // given
        val username: String = "puay124"
        val password: String = "Puay1244"

        // when
        whenever(dataSource.userLogin(username, password)).thenReturn("cuakssssssa6wadidawjiwa")

        val expected = "cuakssssssa6wadidawjiwa"
        val actual = authRepository.userLogin(username, password)

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun setToken() = runTest {
        // given
        val token = "cuakssssssa6wadidawjiwa"

        // when
        whenever(dataSource.setToken(token)).thenReturn(Unit)

        val expected = Unit
        val actual = authRepository.setToken(token)

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getToken() = runTest {
        // given
        val token = "cuakssssssa6wadidawjiwa"

        // when
        whenever(dataSource.getToken()).thenReturn(token)

        val expected = token
        val actual = authRepository.getToken()

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun clearToken() = runTest {
        // when
        whenever(dataSource.clearToken()).thenReturn(Unit)

        val expected = Unit
        val actual = authRepository.clearToken()

        // then
        Assert.assertEquals(expected, actual)
    }
}