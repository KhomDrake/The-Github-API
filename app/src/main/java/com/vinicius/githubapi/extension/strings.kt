package com.vinicius.githubapi.extension

fun String.toDateUS() : String {
    val date = split("T")[0]
    return date.replace("-","/")
}