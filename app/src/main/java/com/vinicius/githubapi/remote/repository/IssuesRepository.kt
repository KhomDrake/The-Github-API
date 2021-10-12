package com.vinicius.githubapi.remote.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vinicius.githubapi.data.network.IssueResponse
import com.vinicius.githubapi.remote.network.GithubApi
import com.vinicius.githubapi.remote.paging.IssuesPagingSource
import kotlinx.coroutines.flow.Flow

class IssuesRepository(private val githubApi: GithubApi) {

    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false,
        prefetchDistance = 3
    )

    fun issuesPaging(
        repo: String,
        firstPageError: MutableLiveData<Throwable>
    ) : Flow<PagingData<IssueResponse>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { IssuesPagingSource(githubApi, repo, firstPageError) }
        ).flow
    }

}