package com.example.mycatcollections.presentation.viewmodel

import androidx.lifecycle.*
import com.example.mycatcollections.domain.model.Cat
import com.example.mycatcollections.domain.repository.CatApiRepository
import kotlinx.coroutines.launch

class CatDetailViewModel(
    private val catApiRepository: CatApiRepository
) : ViewModel() {
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