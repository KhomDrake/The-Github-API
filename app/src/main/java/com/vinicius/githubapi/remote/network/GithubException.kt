package com.vinicius.githubapi.remote.network

import java.io.IOException

abstract class GithubException() : IOException()

class InvalidQException : GithubException()

class PageNumberTooLarge : GithubException()

class DefaultException : GithubException()