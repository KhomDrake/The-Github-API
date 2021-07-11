package com.vinicius.githubapi.di

import com.vinicius.githubapi.remote.network.ClientBuilder
import com.vinicius.githubapi.remote.network.GithubApi
import com.vinicius.githubapi.remote.network.RetrofitBuilder
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single { ClientBuilder.build() }
    single { RetrofitBuilder.build(get()) }
    single { get<Retrofit>().create(GithubApi::class.java) }
}