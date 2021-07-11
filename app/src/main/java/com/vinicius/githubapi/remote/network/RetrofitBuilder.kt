package com.vinicius.githubapi.remote.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    fun build(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            baseUrl("https://api.github.com/search/")
            client(okHttpClient)
            addCallAdapterFactory(CoroutineCallAdapterFactory())
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

}
