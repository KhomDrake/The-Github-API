package com.vinicius.githubapi.data.network

import com.google.gson.annotations.SerializedName

class IssuesResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    val items: List<IssueResponse>
)

class IssueResponse(
    val id: Int,
    @SerializedName("html_url")
    val htmlUrl: String,
    val title: String,
    val number: Int,
    val user: IssueUserResponse,
    val state: String,
    @SerializedName("created_at")
    val createdAt: String?,
    val comments: Int,
    @SerializedName("close_at")
    val closeAt: String? = null
)

class IssueUserResponse(
    val login: String,
    @SerializedName("html_url")
    val htmlUrl: String
)