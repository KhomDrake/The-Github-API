package com.vinicius.githubapi.data.ui

import com.vinicius.githubapi.data.network.UserResponse

data class User(
    val login: String,
    val id: Int,
    val avatarUrl: String,
    val url: String,
    val type: String
) {
    constructor(userResponse: UserResponse) : this(
        userResponse.login,
        userResponse.id,
        userResponse.avatarUrl,
        userResponse.url,
        userResponse.type
    )
}