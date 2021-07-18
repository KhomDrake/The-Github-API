package com.vinicius.githubapi.ui.detail.information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.vinicius.githubapi.data.ui.CommitBody
import com.vinicius.githubapi.remote.repository.CommitsRepository
import kotlinx.coroutines.flow.map

class CommitsViewModel(private val commitsRepository: CommitsRepository) : ViewModel() {

    var repo: String = ""
    var language: String = ""

    fun commitsPaging() = commitsRepository.commitsPaging(
        repo, language
    ).map {
        it.map { commit -> CommitBody(commit) }
    }.cachedIn(viewModelScope)

}