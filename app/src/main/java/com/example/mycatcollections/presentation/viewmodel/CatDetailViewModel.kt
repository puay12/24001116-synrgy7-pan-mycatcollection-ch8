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

class CatDetailViewModel(
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
                    return CatDetailViewModel(
                        catApiRepository = catApiRepository
                    ) as T
                }
            }
    }

    private val _liveCatDetailData: MutableLiveData<Cat> = MutableLiveData()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()

    fun getCatDetail(id: String): LiveData<Cat> {
        loadData(id)
        return _liveCatDetailData
    }

    fun getError(): LiveData<Throwable> {
        return _error
    }

    private fun loadData(id: String) {
        viewModelScope.launch {
            try {
                _liveCatDetailData.value = catApiRepository.getCatById(id)
            } catch (error: Throwable) {
                _error.value = error
            }
        }
    }
}