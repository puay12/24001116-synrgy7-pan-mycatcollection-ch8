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
class CatCollectionsViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private val catApiRepository = mock<CatApiRepository>()
    private val viewModel = CatCollectionsViewModel(
        catApiRepository = catApiRepository
    )
    // observers
    private val catDataObserver = mock<Observer<List<Cat>>>()
    private val loadingObserver = mock<Observer<Boolean>>()
    private val errorObserver = mock<Observer<Throwable>>()
    // captors
    private val loadingCaptor = argumentCaptor<Boolean>()
    private val errorCaptor = argumentCaptor<Throwable>()
    private val catDataCaptor = argumentCaptor<List<Cat>>()

    @Test
    fun loadDataSuccess() = runTest {
        // given
        val loadingLiveData = viewModel.getLoading()
        val catLiveData = viewModel.getCatCollections()
        val cats = listOf<Cat>()

        loadingLiveData.observeForever(loadingObserver)
        catLiveData.observeForever(catDataObserver)

        // when
        whenever(catApiRepository.getCatCollections()).thenReturn(cats)
        viewModel.getCatCollections()

        // then
        verify(loadingObserver, times(3)).onChanged(loadingCaptor.capture())
        verify(catDataObserver, times(2)).onChanged(catDataCaptor.capture())

        Assert.assertEquals(loadingCaptor.allValues, listOf(false, true, false))
        Assert.assertEquals(catDataCaptor.allValues, listOf(null, cats))
    }

    @Test
    fun loadDataFailed() = runTest {
        // given
        val loadingLiveData = viewModel.getLoading()
        val errorLiveData = viewModel.getError()

        loadingLiveData.observeForever(loadingObserver)
        errorLiveData.observeForever(errorObserver)

        // when
        val errorThrowable = UnsupportedOperationException("Not Found")

        whenever(catApiRepository.getCatCollections()).thenThrow(errorThrowable)
        viewModel.getCatCollections()

        // then
        verify(loadingObserver, times(2)).onChanged(loadingCaptor.capture())
        verify(errorObserver).onChanged(errorCaptor.capture())

        Assert.assertEquals(loadingCaptor.allValues, listOf(true, false))
        Assert.assertEquals(errorCaptor.allValues, listOf(errorThrowable))
    }
}