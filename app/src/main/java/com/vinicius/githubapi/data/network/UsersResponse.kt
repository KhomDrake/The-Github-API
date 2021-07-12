package com.vinicius.githubapi.data.network

import com.google.gson.annotations.SerializedName

class UsersResponse(
    @SerializedName("items")
    val users: List<UserResponse>
)

class UserResponse(
    val login: String,
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("html_url")
    val url: String,
    val type: String
)