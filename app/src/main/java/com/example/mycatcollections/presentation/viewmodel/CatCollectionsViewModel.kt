package com.example.mycatcollections.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.mycatcollections.data.datasource.remote.CatRemoteDataSourceImpl
import com.example.mycatcollections.data.datasource.remote.provideCatApiService
import com.example.mycatcollections.data.model.Cat
import com.example.mycatcollections.data.repository.CatApiRepositoryImpl
import com.example.mycatcollections.domain.repository.CatApiRepository
import kotlinx.coroutines.launch

class CatCollectionsViewModel(
    private val catApiRepository: CatApiRepository
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
                    val catApiRepository: CatApiRepository = CatApiRepositoryImpl(
                        catRemoteDataSource = CatRemoteDataSourceImpl(
                            catApiService = provideCatApiService(context)
                        )
                    )
                    return CatCollectionsViewModel(
                        catApiRepository = catApiRepository
                    ) as T
                }
            }
    }

    private val _liveCatCollectionsData: MutableLiveData<List<Cat>> = MutableLiveData()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun getCatCollections(): LiveData<List<Cat>> {
        loadData()
        return _liveCatCollectionsData
    }

    fun getError(): LiveData<Throwable> {
        return _error
    }

    fun getLoading() : LiveData<Boolean> {
        return _loading
    }

    private fun loadData() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _liveCatCollectionsData.value = catApiRepository.getCatCollections()
            } catch (error: Throwable) {
                _error.value = error
            }
            _loading.value = false
        }
    }
}