package com.vinicius.githubapi.remote.network

import com.google.gson.Gson
import com.vinicius.githubapi.data.network.GithubError
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ResponseInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response = chain.proceed(request)

        if(!response.isSuccessful) {
            val body = response.body?.string()
            val gson = Gson()
            val error = runCatching { gson.fromJson(body, GithubError::class.java) }.getOrNull()
            throw when {
                error == null ->
                    DefaultException()
                error.message.contains("Validation Failed") ->
                    InvalidQException()
                else -> DefaultException()
            }
        }

        return response
    }
}
