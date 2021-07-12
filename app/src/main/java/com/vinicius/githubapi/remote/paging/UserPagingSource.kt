package com.vinicius.githubapi.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vinicius.githubapi.data.network.UserResponse
import com.vinicius.githubapi.remote.network.GithubApi

class UserPagingSource(
    private val githubApi: GithubApi,
    private val user: String
) : PagingSource<Int, UserResponse>() {

    override fun getRefreshKey(state: PagingState<Int, UserResponse>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserResponse> {
        val page = params.key ?: DEFAULT_PAGE
        return runCatching {
            val response = githubApi.searchUsersAsync(
                user, page
            )

            LoadResult.Page(
                response.users,
                prevKey = if (page == DEFAULT_PAGE) null else page - 1,
                nextKey = if (20 * page >= 1000) null else page + 1
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }

}