package com.vinicius.githubapi.remote.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vinicius.githubapi.data.network.CommitBodyResponse
import com.vinicius.githubapi.remote.network.GithubApi
import com.vinicius.githubapi.remote.paging.CommitsPagingSource
import kotlinx.coroutines.flow.Flow

class CommitsRepository(private val githubApi: GithubApi) {

    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false,
        prefetchDistance = 3
    )

    fun commitsPaging(
        repo: String,
        language: String,
        firstPageError: MutableLiveData<Throwable>
    ) : Flow<PagingData<CommitBodyResponse>> {
        val q = "repo:$repo+$language"
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { CommitsPagingSource(githubApi, q, firstPageError) }
        ).flow
    }

}