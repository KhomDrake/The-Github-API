package com.vinicius.githubapi.utils

import br.com.arch.toolkit.livedata.response.MutableResponseLiveData
import br.com.arch.toolkit.livedata.response.ResponseLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun <T>livedataAsync(
    liveData: MutableResponseLiveData<T> = MutableResponseLiveData<T>(),
    operation: suspend () -> T
) : ResponseLiveData<T> {
    asyncOperation {
        runCatching {
            liveData.postLoading()
            liveData.postData(operation.invoke())
        }.onFailure {
            liveData.postError(it)
        }
    }

    return liveData
}
