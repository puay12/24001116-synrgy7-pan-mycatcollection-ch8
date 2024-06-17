package com.example.mycatcollections.data.datasource.remote

import com.example.mycatcollections.data.datasource.services.CatApiService
import com.example.mycatcollections.data.model.CatResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CatRemoteDataSourceImplTest {
    private val catApiService = mock<CatApiService>()
    private val catRemoteDataSource = CatRemoteDataSourceImpl(
        catApiService = catApiService
    )

    @Test
    fun fetchCatData() = runTest {
        // given
        val listOfCatResponse = listOf<CatResponse>()

        // when
        whenever(catApiService.getCatCollections()).thenReturn(listOfCatResponse)

        val expected = listOfCatResponse
        val actual = catRemoteDataSource.fetchCatData()

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fetchCatDataById() = runTest {
        // given
        val id = "1"
        val catResponse = CatResponse(
            width = null, id = null, url = null, breeds = null, height = null
        )

        // when
        whenever(catApiService.getCatById(id)).thenReturn(catResponse)

        val expected = catResponse
        val actual = catRemoteDataSource.fetchCatDataById(id)

        // then
        Assert.assertEquals(expected, actual)
    }
}