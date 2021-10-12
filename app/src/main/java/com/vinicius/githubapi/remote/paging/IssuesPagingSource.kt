package com.vinicius.githubapi.remote.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vinicius.githubapi.data.network.IssueResponse
import com.vinicius.githubapi.remote.network.GithubApi

class IssuesPagingSource(
    private val githubApi: GithubApi,
    private val repo: String,
    private val firstPageError: MutableLiveData<Throwable>
) : PagingSource<Int, IssueResponse>() {

    override fun getRefreshKey(state: PagingState<Int, IssueResponse>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IssueResponse> {
        val page = params.key ?: DEFAULT_PAGE
        return runCatching {
            val response = githubApi.searchIssuesAsync(
                "repo:$repo", page, PER_PAGE
            )

            val nextKey = if (page * PER_PAGE >= response.totalCount) null else page + 1

            LoadResult.Page(
                response.items,
                prevKey = if (page == DEFAULT_PAGE) null else page - 1,
                nextKey = if(response.totalCount == 0) null else nextKey
            )
        }.getOrElse {
            if(page == DEFAULT_PAGE)
                firstPageError.postValue(it)

            LoadResult.Error(it)
        }
    }

}