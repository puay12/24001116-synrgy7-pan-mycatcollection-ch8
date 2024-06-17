package com.example.mycatcollections.data.repository

import com.example.mycatcollections.data.datasource.interfaces.CatRemoteDataSource
import com.example.mycatcollections.data.model.BreedsItem
import com.example.mycatcollections.data.model.CatResponse
import com.example.mycatcollections.data.model.Weight
import com.example.mycatcollections.domain.model.Cat
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CatApiRepositoryImplTest {
    private val catRemoteDataSource = mock<CatRemoteDataSource>()
    private val catApiRepository = CatApiRepositoryImpl(
        catRemoteDataSource = catRemoteDataSource
    )

    @Test
    fun getCatCollections() = runTest {
        // given
        val listOfCatResponse = listOf<CatResponse>()
        val listOfCat = listOf<Cat>()

        // when
        whenever(catRemoteDataSource.fetchCatData()).thenReturn(listOfCatResponse)

        val expected = listOfCat
        val actual = catApiRepository.getCatCollections()

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getCatById() = runTest {
        // given
        val id = "1"
        val breeds = listOf<BreedsItem>(
            BreedsItem(
                description = "1",
                origin = "1",
                name = "1",
                lifeSpan = "1",
                temperament = "1",
                weight = Weight(metric = "1"),
            )
        )
        val cat = Cat(
            id = "1",
            imgUrl = "1",
            name = "1",
            weight = "1",
            lifeSpan = "1",
            temperament = "1",
            origin = "1",
            description = "1"
        )
        val catResponse = CatResponse(
            width = 1, id = "1", url = "1", breeds = breeds, height = 1
        )

        // when
        whenever(catRemoteDataSource.fetchCatDataById(id)).thenReturn(catResponse)

        val expected = cat
        val actual = catApiRepository.getCatById(id)

        // then
        Assert.assertEquals(expected, actual)
    }
}