package com.vinicius.githubapi.remote.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vinicius.githubapi.data.network.RepositoryResponse
import com.vinicius.githubapi.remote.network.GithubApi
import com.vinicius.githubapi.remote.paging.RepositoryPagingSource
import kotlinx.coroutines.flow.Flow

const val PAGE_SIZE = 20

class ReposRepository(private val githubApi: GithubApi) {

    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false,
        prefetchDistance = 3
    )

    fun repositoriesPaging(
        language: String,
        onError: MutableLiveData<Throwable>
    ) : Flow<PagingData<RepositoryResponse>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { RepositoryPagingSource(githubApi, language, onError) }
        ).flow
    }
}