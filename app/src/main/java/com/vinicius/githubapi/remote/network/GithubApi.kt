package com.vinicius.githubapi.remote.network

import com.vinicius.githubapi.data.network.RepositoriesResponse
import com.vinicius.githubapi.data.network.RepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val SORT_DEFAULT = "stars"

interface GithubApi {

    @GET("repositories")
    suspend fun searchReposAsync(
        @Query("q") language: String,
        @Query("page") page: Int,
        @Query("sort") sort: String = SORT_DEFAULT
    ) : RepositoriesResponse

    @GET("commits")
    suspend fun searchCommitsAsync(
        @Query("q") repository: String
    ) : RepositoryResponse

    @GET("issues")
    suspend fun searchIssuesAsync(
        @Query("q") repository: String
    ) : RepositoryResponse

    @GET("users")
    suspend fun searchUsersAsync(
        @Query("q") users: String
    ) : RepositoryResponse

}
