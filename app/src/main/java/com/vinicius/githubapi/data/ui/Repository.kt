package com.vinicius.githubapi.data.ui

import android.os.Parcelable
import com.vinicius.githubapi.data.network.LicenseResponse
import com.vinicius.githubapi.data.network.OwnerRepositoryResponse
import com.vinicius.githubapi.data.network.RepositoriesResponse
import com.vinicius.githubapi.data.network.RepositoryResponse
import kotlinx.parcelize.Parcelize

class Repositories(
    val items: List<Repository>
) {
    constructor(repositoriesResponse: RepositoriesResponse) : this(repositoriesResponse.items.map {
        Repository(it)
    })
}

@Parcelize
class Repository(
    val id: Int,
    val name: String,
    val fullName: String,
    val owner: OwnerRepository,
    val description: String?,
    val isFork: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val defaultBranch: String,
    val score: Double,
    val watchers: Int,
    val openIssues: Int,
    val language: String?,
    val stars: String,
    val forks: Int,
    val license: License?
) : Parcelable {
    constructor(repositoryResponse: RepositoryResponse) : this(
        repositoryResponse.id,
        repositoryResponse.name,
        repositoryResponse.fullName,
        OwnerRepository(repositoryResponse.owner),
        repositoryResponse.description,
        repositoryResponse.isFork,
        repositoryResponse.createdAt,
        repositoryResponse.updatedAt,
        repositoryResponse.defaultBranch,
        repositoryResponse.score,
        repositoryResponse.watchers,
        repositoryResponse.openIssues,
        repositoryResponse.language,
        repositoryResponse.stars.toString(),
        repositoryResponse.forks,
        repositoryResponse.license?.run {
            License(repositoryResponse.license)
        }
    )
}

@Parcelize
class License(
    val url: String?,
    val key: String,
    val name: String
) : Parcelable {

    constructor(licenseResponse: LicenseResponse): this(
        licenseResponse.url,
        licenseResponse.key,
        licenseResponse.name
    )
}

@Parcelize
class OwnerRepository(
    val login: String,
    val idOwner: Int,
    val avatarUrl: String?,
    val type: String
) : Parcelable {
    constructor(owner: OwnerRepositoryResponse): this(
        owner.login,
        owner.idOwner,
        owner.avatarUrl,
        owner.type
    )
}