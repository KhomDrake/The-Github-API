package com.vinicius.githubapi.data.network

import com.google.gson.annotations.SerializedName

class CommitsResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    val items: List<CommitBodyResponse>
)

class CommitBodyResponse(
    @SerializedName("sha")
    val id: String,
    @SerializedName("html_url")
    val htmlUrl: String?,
    val author: AuthorResponse?,
    val commit: CommitResponse?,
    val committer: CommitterResponse?
)

class AuthorResponse(
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("html_url")
    val htmlUrl: String?
)

class CommitResponse(
    @SerializedName("comment_count")
    val comments: Int,
    val message: String?,
    val author: CommitAuthorResponse,
    val committer: CommitCommitterResponse
)

class CommitAuthorResponse(
    val date: String,
    val name: String
)

class CommitCommitterResponse(
    val date: String,
    val name: String
)

class CommitterResponse(
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("html_url")
    val htmlUrl: String?
)