package com.example.mycatcollections.data.datasource.services

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.mycatcollections.data.BuildConfig
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_KEY = "live_bTtRON9fg6BSeIIqQRie0Ze7NSuD12HC1POuzleVETEGYiQgYIbi5jVqXJAwcnlo"

private fun provideRetrofitBuilder(context: Context, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(provideOkhttpClient(context))
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()
}

private fun provideOkhttpClient(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(provideHttpLoggingInterceptor())
        .addInterceptor(provideChuckerInterceptor(context))
        .build()
}

private fun provideHttpLoggingInterceptor(): Interceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoggingInterceptor
}

private fun provideChuckerInterceptor(context: Context): Interceptor {
    return ChuckerInterceptor.Builder(context).build()
}

fun provideCatApiService(context: Context) : CatApiService {
    return provideRetrofitBuilder(
        context,
        BuildConfig.BASE_URL
    ).create(CatApiService::class.java)
}