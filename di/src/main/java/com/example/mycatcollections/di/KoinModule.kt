package com.example.mycatcollections.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.mycatcollections.data.datasource.interfaces.AuthLocalDataSource
import com.example.mycatcollections.data.datasource.interfaces.CatRemoteDataSource
import com.example.mycatcollections.data.datasource.local.AuthLocalDataSourceImpl
import com.example.mycatcollections.data.datasource.remote.CatRemoteDataSourceImpl
import com.example.mycatcollections.data.datasource.remote.provideCatApiService
import com.example.mycatcollections.data.datasource.services.CatApiService
import com.example.mycatcollections.data.datasource.services.datastore
import com.example.mycatcollections.data.repository.AuthRepositoryImpl
import com.example.mycatcollections.data.repository.CatApiRepositoryImpl
import com.example.mycatcollections.domain.model.Cat
import com.example.mycatcollections.domain.repository.AuthRepository
import com.example.mycatcollections.domain.repository.CatApiRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val koinModule = module {
    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(authLocalDataSource = get()) }
    single<CatApiRepository> { CatApiRepositoryImpl(catRemoteDataSource = get()) }

    // Data Sources
    single<AuthLocalDataSource> { AuthLocalDataSourceImpl(dataStore = get()) }
    single<CatRemoteDataSource> { CatRemoteDataSourceImpl(catApiService = get()) }

    // Services
    single<CatApiService> { provideCatApiService(androidContext()) }

    // Data store
    single<DataStore<Preferences>> { androidContext().datastore }
}