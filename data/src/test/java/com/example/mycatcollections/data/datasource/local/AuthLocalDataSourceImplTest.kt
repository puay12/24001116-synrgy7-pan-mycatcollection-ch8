package com.example.mycatcollections.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AuthLocalDataSourceImplTest {
    private val dataStore = mock<DataStore<Preferences>>()
    private val dataSourceImpl = AuthLocalDataSourceImpl(
        dataStore = dataStore
    )
    private val companion = mock<AuthLocalDataSourceImpl.Companion>()

    @Test
    fun userLogin() = runTest {
        // given
        val username: String = "puay124"
        val password: String = "Puay1244"

        // when
        val actual = dataSourceImpl.userLogin(username, password)
        val expected = "cuakssssssa6wadidawjiwa"

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun setToken() = runTest {
        // given
        val token = "cuakssssssa6wadidawjiwa"

        // when
        val expected = Unit
        val actual = dataSourceImpl.setToken(token)

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getToken() = runTest {
        // given
        val token = "cuakssssssa6wadidawjiwa"
        val key = stringPreferencesKey(companion.KEY_TOKEN)

        // when
        whenever(dataStore.data).thenReturn(
            flowOf(
                preferencesOf(
                    key to token
                )
            )
        )

        val expected = token
        val actual = dataSourceImpl.getToken()

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun clearToken() = runTest {
        // when
        val expected = Unit
        val actual = dataSourceImpl.clearToken()

        // then
        Assert.assertEquals(expected, actual)
    }
}