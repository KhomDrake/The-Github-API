package com.vinicius.githubapi.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.vinicius.githubapi.data.ui.User
import com.vinicius.githubapi.remote.repository.UsersRepository
import kotlinx.coroutines.flow.map

class SearchViewModel(private val repository: UsersRepository): ViewModel() {

    fun searchUser(user: String) = repository.searchUser(user)
        .map { it.map { userResponse -> User(userResponse) } }
        .cachedIn(viewModelScope)

}
