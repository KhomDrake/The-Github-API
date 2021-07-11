package com.vinicius.githubapi.di

import com.vinicius.githubapi.remote.repository.CommitsRepository
import com.vinicius.githubapi.remote.repository.IssuesRepository
import com.vinicius.githubapi.remote.repository.ReposRepository
import com.vinicius.githubapi.remote.repository.UsersRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { CommitsRepository(get()) }
    single { IssuesRepository(get()) }
    single { ReposRepository(get()) }
    single { UsersRepository(get()) }
}