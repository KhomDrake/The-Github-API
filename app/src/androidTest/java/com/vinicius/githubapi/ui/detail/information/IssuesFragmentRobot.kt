package com.vinicius.githubapi.ui.detail.information

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import com.vinicius.githubapi.R
import com.vinicius.githubapi.data.network.IssueResponse
import com.vinicius.githubapi.data.network.IssueUserResponse
import com.vinicius.githubapi.data.network.IssuesResponse
import com.vinicius.githubapi.remote.network.GithubApi
import com.vinicius.githubapi.ui.detail.REPOSITORY_FULL_NAME_ARGS
import com.vinicius.githubapi.utils.Check
import com.vinicius.githubapi.utils.Launch
import com.vinicius.githubapi.utils.Setup
import com.vinicius.githubapi.utils.extension.*
import io.mockk.coEvery
import io.mockk.coVerify
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun IssuesFragmentTest.setup(func: IssuesFragmentSetup.() -> Unit) =
    IssuesFragmentSetup().apply(func)

private const val repo = "square/okhttp"

private val repository = "repo:$repo"

class IssuesFragmentSetup : Setup<IssuesFragmentLaunch, IssuesFragmentCheck>, KoinComponent {

    private val githubApi: GithubApi by inject()

    private val args = Bundle().apply {
        putString(REPOSITORY_FULL_NAME_ARGS, repo)
    }

    override fun createCheck(): IssuesFragmentCheck {
        return IssuesFragmentCheck()
    }

    override fun createLaunch(): IssuesFragmentLaunch {
        return IssuesFragmentLaunch()
    }

    override fun setupLaunch() {
        launchFragmentInContainer<IssuesFragment>(args, themeResId = R.style.Theme_GithubApi)
        Thread.sleep(500)
    }

    private fun getIssue(id: Int) =
        IssueResponse(
            id,
            "https://github.com/square/okhttp/issues/6879",
            "Broken Link on OkHttp homepage",
            6878,
            IssueUserResponse(
                "clementdessoude",
                "https://github.com/clementdessoude"
            ),
            "open",
            "2021-10-11T15:39:45Z",
            0,
            null
        )

    fun withLoadIssuesFirstPage() {
        coEvery {
            githubApi.searchIssuesAsync(
                repository, 1, any()
            )
        } returns IssuesResponse(
            1,
            listOf(
                getIssue(1)
            )
        )
    }

    fun withLoadIssuesTwoPages() {
        coEvery {
            githubApi.searchIssuesAsync(
                repository, 1, any()
            )
        } returns IssuesResponse(
            12,
            listOf(
                getIssue(1),
                getIssue(2),
                getIssue(3),
                getIssue(4),
                getIssue(5),
                getIssue(6),
                getIssue(7),
                getIssue(8),
                getIssue(9),
                getIssue(10)
            )
        )

        coEvery {
            githubApi.searchIssuesAsync(
                repository, 2, any()
            )
        } returns IssuesResponse(
            12,
            listOf(
                getIssue(11),
                getIssue(12)
            )
        )
    }

    fun withLoadIssuesError() {
        coEvery {
            githubApi.searchIssuesAsync(
                repository, 1, any()
            )
        } throws Exception()
    }

}

class IssuesFragmentLaunch : Launch<IssuesFragmentCheck> {

    override fun createCheck(): IssuesFragmentCheck {
        return IssuesFragmentCheck()
    }

    fun scrollToLastItem() {
        R.id.issues.scrollToPosition(9)
    }

    fun clickAccessIssue() {
        "https://github.com/square/okhttp/issues/6879".mockLinkOpening()
        R.id.access.click()
    }

    fun clickTryAgain() {
        R.id.error_button.click()
    }

}

class IssuesFragmentCheck : Check, KoinComponent {

    private val githubApi: GithubApi by inject()

    fun loadIssuesFirstPage() {
        coVerify {
            githubApi.searchIssuesAsync(
                repository, 1, any()
            )
        }
    }

    fun loadIssuesSecondPage() {
        coVerify {
            githubApi.searchIssuesAsync(
                repository, 2, any()
            )
        }
    }

    fun issuesDisplayed() {
        R.id.title.hasText("Title: Broken Link on OkHttp homepage")
        R.id.author.hasText("Author: clementdessoude")
        R.id.state.hasText("State: open")
        R.id.date.hasText("Created: 2021/10/11")
        R.id.access.isDisplayed()
    }

    fun openIssueLink() {
        "https://github.com/square/okhttp/issues/6879".checkLinkOpening()
    }

    fun errorFirstPageDisplayed() {
        errorViewDisplayed()
    }

    fun loadIssuesFirstPageTwoTimes() {
        coVerify(exactly = 2) {
            githubApi.searchIssuesAsync(
                repository, 1, any()
            )
        }
    }

}