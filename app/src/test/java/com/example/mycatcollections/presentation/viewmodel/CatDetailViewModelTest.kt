package com.example.mycatcollections.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mycatcollections.MainDispatcherRule
import com.example.mycatcollections.domain.model.Cat
import com.example.mycatcollections.domain.repository.CatApiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class CatDetailViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private val catApiRepository = mock<CatApiRepository>()
    private val viewModel = CatDetailViewModel(
        catApiRepository = catApiRepository
    )
    // observers
    private val catDataObserver = mock<Observer<Cat>>()
    private val errorObserver = mock<Observer<Throwable>>()
    // captors
    private val errorCaptor = argumentCaptor<Throwable>()
    private val catDataCaptor = argumentCaptor<Cat>()

    @Test
    fun loadDataSuccess() = runTest {
        // given
        val id = "1"
        val catLiveData = viewModel.getCatDetail(id)
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

        catLiveData.observeForever(catDataObserver)

        // when
        whenever(catApiRepository.getCatById(id)).thenReturn(cat)
        viewModel.getCatDetail(id)

        // then
        verify(catDataObserver, times(2)).onChanged(catDataCaptor.capture())

        Assert.assertEquals(catDataCaptor.allValues, listOf(null, cat))
    }

    @Test
    fun loadDataFailed() = runTest {
        // given
        val errorLiveData = viewModel.getError()
        val id = "1"

        errorLiveData.observeForever(errorObserver)

        // when
        val errorThrowable = UnsupportedOperationException("Not Found")

        whenever(catApiRepository.getCatById(id)).thenThrow(errorThrowable)
        viewModel.getCatDetail(id)

        // then
        verify(errorObserver).onChanged(errorCaptor.capture())

        Assert.assertEquals(errorCaptor.allValues, listOf(errorThrowable))
    }
}