package com.vinicius.githubapi.remote.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vinicius.githubapi.remote.network.GithubApi
import com.vinicius.githubapi.remote.paging.UserPagingSource

class UsersRepository(private val githubApi: GithubApi) {

    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false,
        prefetchDistance = 3
    )

    fun searchUser(user: String, mutableLiveData: MutableLiveData<Throwable>) = Pager(
        config = pagingConfig,
        pagingSourceFactory = { UserPagingSource(githubApi, user, mutableLiveData) }
    ).flow

}