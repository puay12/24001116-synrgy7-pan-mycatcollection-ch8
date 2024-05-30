package com.example.mycatcollections.diviewmodel

import com.example.mycatcollections.presentation.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(authRepository = get()) }
    viewModel { CatCollectionsViewModel(catApiRepository = get()) }
    viewModel { CatDetailViewModel(catApiRepository = get()) }
    viewModel { MainViewModel(authRepository = get()) }
    viewModel { NavigatorViewModel(authRepository = get()) }
}