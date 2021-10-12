package com.vinicius.githubapi.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.vinicius.githubapi.data.ui.User
import com.vinicius.githubapi.remote.repository.UsersRepository
import kotlinx.coroutines.flow.map

class SearchViewModel(private val repository: UsersRepository): ViewModel() {

    private var user = ""
    private val _error: MutableLiveData<Throwable> = MutableLiveData()

    val error: LiveData<Throwable>
        get() = _error

    fun setUser(newUser: String) {
        user = newUser
    }

    fun searchUser() =
        repository.searchUser(user, _error)
            .map { it.map { userResponse -> User(userResponse) } }
            .cachedIn(viewModelScope)

}
