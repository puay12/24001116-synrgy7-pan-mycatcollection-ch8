package com.example.mycatcollections.presentation.viewmodel

import androidx.lifecycle.*
import com.example.mycatcollections.domain.model.Cat
import com.example.mycatcollections.domain.repository.CatApiRepository
import kotlinx.coroutines.launch

class CatCollectionsViewModel(
    private val catApiRepository: CatApiRepository
) : ViewModel() {

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