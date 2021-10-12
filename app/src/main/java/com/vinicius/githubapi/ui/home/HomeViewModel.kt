package com.vinicius.githubapi.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.vinicius.githubapi.data.ui.Repository
import com.vinicius.githubapi.remote.repository.ReposRepository
import com.vinicius.githubapi.ui.PagingViewModel
import kotlinx.coroutines.flow.map

private const val DEFAULT_LANGUAGE = "kotlin"

class HomeViewModel(private val repository: ReposRepository) : PagingViewModel() {

    var selectedLanguage: String = DEFAULT_LANGUAGE
        private set

    fun updateSelectedLanguage(newLanguage: String) {
        selectedLanguage = newLanguage
    }

    fun repositoriesPaging() = repository.repositoriesPaging(selectedLanguage, _error)
        .map {
            it.map { repositoryResponse -> Repository(repositoryResponse) }
        }.cachedIn(viewModelScope)

}
