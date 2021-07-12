package com.vinicius.githubapi.ui.detail

import androidx.lifecycle.ViewModel
import com.vinicius.githubapi.remote.repository.CommitsRepository
import com.vinicius.githubapi.remote.repository.IssuesRepository

class DetailViewModel(
    private val commitsRepository: CommitsRepository,
    private val issuesRepository: IssuesRepository
) : ViewModel() {



}