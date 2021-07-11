package com.vinicius.githubapi.remote.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vinicius.githubapi.data.network.RepositoryResponse

const val DEFAULT_PAGE = 1

class RepositoryPagingSource(
    private val githubApi: GithubApi,
    private val language: String
) :
    PagingSource<Int, RepositoryResponse>() {

    override fun getRefreshKey(state: PagingState<Int, RepositoryResponse>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryResponse> {
        val page = params.key ?: DEFAULT_PAGE
        return runCatching {
            val response = githubApi.searchReposAsync(
                "language:$language", page
            )

            LoadResult.Page(
                response.items,
                prevKey = if (page == DEFAULT_PAGE) null else page - 1,
                nextKey = if (20 * page >= 1000) null else page + 1
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}
