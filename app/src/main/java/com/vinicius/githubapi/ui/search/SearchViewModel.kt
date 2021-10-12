package com.vinicius.githubapi.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.vinicius.githubapi.data.ui.User
import com.vinicius.githubapi.remote.repository.UsersRepository
import com.vinicius.githubapi.ui.PagingViewModel
import kotlinx.coroutines.flow.map

class SearchViewModel(private val repository: UsersRepository): PagingViewModel() {

    private var user = ""

    fun setUser(newUser: String) {
        user = newUser
    }

    fun searchUser() =
        repository.searchUser(user, _error)
            .map { it.map { userResponse -> User(userResponse) } }
            .cachedIn(viewModelScope)

}
