package com.example.mycatcollections.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.mycatcollections.data.datasource.local.AuthLocalDataSourceImpl
import com.example.mycatcollections.data.datasource.services.datastore
import com.example.mycatcollections.data.repository.AuthRepositoryImpl
import com.example.mycatcollections.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class NavigatorViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    companion object {
        fun provideFactory(
            owner: SavedStateRegistryOwner,
            context: Context,
        ) =
            object : AbstractSavedStateViewModelFactory(owner, null) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle,
                ): T {
                    val authRepository: AuthRepository = AuthRepositoryImpl(
                        authLocalDataSource = AuthLocalDataSourceImpl(
                            dataStore = context.datastore
                        ),
                    )
                    return NavigatorViewModel(
                        authRepository = authRepository
                    ) as T
                }
            }
    }

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