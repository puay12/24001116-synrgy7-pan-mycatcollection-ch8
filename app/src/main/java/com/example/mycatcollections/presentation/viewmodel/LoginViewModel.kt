package com.example.mycatcollections.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.mycatcollections.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _success: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun userLogin(username: String, password: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val token = authRepository.userLogin(username, password)
                authRepository.setToken(token)
                _loading.value = false
                _success.value = true
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }
    }

    fun getError() : LiveData<Throwable> {
        return _error
    }

    fun getSuccess() : LiveData<Boolean> {
        return _success
    }

    fun getLoading() : LiveData<Boolean> {
        return _loading
    }
}