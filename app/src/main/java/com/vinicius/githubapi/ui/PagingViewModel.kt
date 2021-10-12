package com.vinicius.githubapi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class PagingViewModel : ViewModel() {

    protected val _error: MutableLiveData<Throwable> = MutableLiveData()

    val error: LiveData<Throwable>
        get() = _error

}