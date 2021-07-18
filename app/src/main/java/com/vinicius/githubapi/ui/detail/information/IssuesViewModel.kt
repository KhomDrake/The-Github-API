package com.vinicius.githubapi.ui.detail.information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.vinicius.githubapi.data.ui.Issue
import com.vinicius.githubapi.remote.repository.IssuesRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class IssuesViewModel(private val issuesRepository: IssuesRepository) : ViewModel() {

    var repo: String = ""

    fun issuesPaging() = issuesRepository.issuesPaging(repo)
        .map {
            it.map { issue -> Issue(issue) }
        }.catch {

        }.cachedIn(viewModelScope)

}