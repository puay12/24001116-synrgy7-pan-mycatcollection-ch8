package com.example.mycatcollections.presentation.viewmodel

import androidx.lifecycle.*
import com.example.mycatcollections.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class NavigatorViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _isLoggedIn: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun getIsLoggedIn() : LiveData<Boolean> {
        checkUser()
        return _isLoggedIn
    }

    private fun checkUser() {
        viewModelScope.launch {
            try {
                _isLoggedIn.value = !authRepository.getToken().isNullOrEmpty()
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }
}