package com.example.mycatcollections.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.mycatcollections.data.datasource.local.AuthLocalDataSourceImpl
import com.example.mycatcollections.data.datasource.services.datastore
import com.example.mycatcollections.data.repository.AuthRepositoryImpl
import com.example.mycatcollections.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class MainViewModel(
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
                    return MainViewModel(
                        authRepository = authRepository
                    ) as T
                }
            }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearToken()
        }
    }

}