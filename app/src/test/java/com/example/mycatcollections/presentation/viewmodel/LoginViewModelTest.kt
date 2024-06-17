package com.example.mycatcollections.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mycatcollections.MainDispatcherRule
import com.example.mycatcollections.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private val authRepository = mock<AuthRepository>()
    private val loginViewModel = LoginViewModel(
        authRepository = authRepository
    )
    // observers
    private val loadingObserver = mock<Observer<Boolean>>()
    private val errorObserver = mock<Observer<Throwable>>()
    private val successObserver = mock<Observer<Boolean>>()

    // captors
    private val loadingCaptor = argumentCaptor<Boolean>()
    private val errorCaptor = argumentCaptor<Throwable>()
    private val successCaptor = argumentCaptor<Boolean>()

    private val authRepoReturnToken = "cuakssssssa6wadidawjiwa"

    @Test
    fun userLoginSuccess() = runTest {
        // given
        val username = "puay124"
        val password = "Puay1244"
        val loadingLiveData = loginViewModel.getLoading()
        val successLiveData = loginViewModel.getSuccess()

        loadingLiveData.observeForever(loadingObserver)
        successLiveData.observeForever(successObserver)

        // when
        whenever(authRepository.userLogin(username, password)).thenReturn(authRepoReturnToken)
        whenever(authRepository.setToken(authRepoReturnToken)).thenReturn(Unit)
        loginViewModel.userLogin(username, password)

        // then
        verify(loadingObserver, times(2)).onChanged(loadingCaptor.capture())
        verify(successObserver).onChanged(successCaptor.capture())

        Assert.assertEquals(loadingCaptor.allValues, listOf(true, false))
        Assert.assertEquals(successCaptor.allValues, listOf(true))
    }

    @Test
    fun userLoginFailed() = runTest {
        // given
        val username = "puay124"
        val password = "Puay1244"
        val loadingLiveData = loginViewModel.getLoading()
        val errorLiveData = loginViewModel.getError()

        loadingLiveData.observeForever(loadingObserver)
        errorLiveData.observeForever(errorObserver)

        // when
        val errorThrowable = UnsupportedOperationException("Maaf, user ini tidak ditemukan")

        whenever(authRepository.userLogin(username, password)).thenThrow(errorThrowable)
        loginViewModel.userLogin(username, password)

        // then
        verify(errorObserver).onChanged(errorCaptor.capture())
        verify(loadingObserver, times(2)).onChanged(loadingCaptor.capture())

        Assert.assertEquals(loadingCaptor.allValues, listOf(true, false))
        Assert.assertEquals(errorCaptor.allValues, listOf(errorThrowable))
    }
}