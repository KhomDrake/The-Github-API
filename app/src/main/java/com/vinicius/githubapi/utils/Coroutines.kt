package com.vinicius.githubapi.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

val githubCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

fun asyncOperation(operation: suspend () -> Unit) {
    githubCoroutineScope.launch {
        operation.invoke()
    }
}