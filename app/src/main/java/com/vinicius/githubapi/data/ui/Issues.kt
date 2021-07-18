package com.vinicius.githubapi.data.ui

import com.vinicius.githubapi.data.network.IssueResponse
import com.vinicius.githubapi.data.network.IssueUserResponse

data class Issue(
    val id: Int,
    val htmlUrl: String,
    val title: String,
    val number: Int,
    val user: IssueUser,
    val state: String,
    val createdAt: String?,
    val comments: Int,
    val closeAt: String? = null
) {
    constructor(issueResponse: IssueResponse) : this(
        issueResponse.id,
        issueResponse.htmlUrl,
        issueResponse.title,
        issueResponse.number,
        IssueUser(issueResponse.user),
        issueResponse.state,
        issueResponse.createdAt,
        issueResponse.comments,
        issueResponse.closeAt
    )
}

class IssueUser(
    val login: String,
    val htmlUrl: String
) {
    constructor(issueUserResponse: IssueUserResponse) : this(
        issueUserResponse.login,
        issueUserResponse.htmlUrl
    )
}