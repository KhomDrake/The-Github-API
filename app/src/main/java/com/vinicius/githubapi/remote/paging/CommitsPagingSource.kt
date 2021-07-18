package com.vinicius.githubapi.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vinicius.githubapi.data.network.CommitBodyResponse
import com.vinicius.githubapi.remote.network.GithubApi

const val PER_PAGE = 10

class CommitsPagingSource(
    private val githubApi: GithubApi,
    private val q: String
) : PagingSource<Int, CommitBodyResponse>() {
    override fun getRefreshKey(state: PagingState<Int, CommitBodyResponse>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommitBodyResponse> {
        val page = params.key ?: DEFAULT_PAGE
        return runCatching {
            val response = githubApi.searchCommitsAsync(
                q, page, PER_PAGE
            )

            val nextKey = if (page * PER_PAGE >= response.totalCount) null else page + 1

            LoadResult.Page(
                response.items,
                prevKey = if (page == DEFAULT_PAGE) null else page - 1,
                nextKey = if(response.totalCount == 0) null else nextKey
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}