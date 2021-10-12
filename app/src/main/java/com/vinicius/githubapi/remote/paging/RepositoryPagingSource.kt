package com.vinicius.githubapi.remote.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vinicius.githubapi.data.network.RepositoryResponse
import com.vinicius.githubapi.remote.network.GithubApi

const val DEFAULT_PAGE = 1

class RepositoryPagingSource(
    private val githubApi: GithubApi,
    private val language: String,
    private val errorFirstPage: MutableLiveData<Throwable>
) :
    PagingSource<Int, RepositoryResponse>() {

    override fun getRefreshKey(state: PagingState<Int, RepositoryResponse>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryResponse> {
        val page = params.key ?: DEFAULT_PAGE
        return runCatching {
            val response = githubApi.searchReposAsync(
                "language:$language", page
            )

            val nextKey = if (page * PER_PAGE >= response.totalCount) null else page + 1

            LoadResult.Page(
                response.items,
                prevKey = if (page == DEFAULT_PAGE) null else page - 1,
                nextKey = if(response.totalCount == 0) null else nextKey
            )
        }.getOrElse {
            if(page == DEFAULT_PAGE)
                errorFirstPage.postValue(it)

            LoadResult.Error(it)
        }
    }
}
