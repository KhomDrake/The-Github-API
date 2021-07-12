package com.vinicius.githubapi.di

import com.vinicius.githubapi.ui.detail.DetailViewModel
import com.vinicius.githubapi.ui.home.HomeViewModel
import com.vinicius.githubapi.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { DetailViewModel(get(), get()) }
}