package com.vinicius.githubapi.data.ui

import com.vinicius.githubapi.data.network.CommitCommitterResponse
import com.vinicius.githubapi.data.network.AuthorResponse
import com.vinicius.githubapi.data.network.CommitAuthorResponse
import com.vinicius.githubapi.data.network.CommitBodyResponse
import com.vinicius.githubapi.data.network.CommitResponse
import com.vinicius.githubapi.data.network.CommitterResponse

data class CommitBody(
    val id: String,
    val htmlUrl: String?,
    val author: Author?,
    val commit: Commit?,
    val committer: Committer?
) {
    constructor(commitBodyResponse: CommitBodyResponse): this(
        commitBodyResponse.id,
        commitBodyResponse.htmlUrl,
        commitBodyResponse.author?.run {
            Author(commitBodyResponse.author)
        },
        commitBodyResponse.commit?.run {
            Commit(commitBodyResponse.commit)
        },
        commitBodyResponse.committer?.run {
            Committer(commitBodyResponse.committer)
        }
    )
}

class Author(
    val login: String,
    val avatarUrl: String?,
    val htmlUrl: String?
) {
    constructor(authorResponse: AuthorResponse) : this(
        authorResponse.login,
        authorResponse.avatarUrl,
        authorResponse.htmlUrl
    )
}

class Commit(
    val comments: Int,
    val message: String?,
    val author: CommitAuthor,
    val committer: CommitCommitter
) {
    constructor(commitResponse: CommitResponse) : this(
        commitResponse.comments,
        commitResponse.message,
        CommitAuthor(commitResponse.author),
        CommitCommitter(commitResponse.committer)
    )
}

class CommitAuthor(
    val date: String,
    val name: String
) {
    constructor(commitAuthorResponse: CommitAuthorResponse) : this(
        commitAuthorResponse.date,
        commitAuthorResponse.name
    )
}

class CommitCommitter(
    val date: String,
    val name: String
) {
    constructor(commitCommitterResponse: CommitCommitterResponse) : this(
        commitCommitterResponse.date,
        commitCommitterResponse.name
    )
}

class Committer(
    val login: String,
    val avatarUrl: String?,
    val htmlUrl: String?
) {
   constructor(committerResponse: CommitterResponse) : this(
       committerResponse.login,
       committerResponse.avatarUrl,
       committerResponse.htmlUrl
   )
}