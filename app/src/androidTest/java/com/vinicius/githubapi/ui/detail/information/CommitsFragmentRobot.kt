package com.vinicius.githubapi.ui.detail.information

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import com.vinicius.githubapi.R
import com.vinicius.githubapi.data.network.*
import com.vinicius.githubapi.remote.network.GithubApi
import com.vinicius.githubapi.ui.detail.LANGUAGE_REPOSITORY_ARGS
import com.vinicius.githubapi.ui.detail.REPOSITORY_FULL_NAME_ARGS
import com.vinicius.githubapi.utils.Check
import com.vinicius.githubapi.utils.Launch
import com.vinicius.githubapi.utils.Setup
import com.vinicius.githubapi.utils.extension.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun CommitsFragmentTest.setup(func: CommitsFragmentSetup.() -> Unit) =
    CommitsFragmentSetup().apply(func)

class CommitsFragmentSetup : Setup<CommitsFragmentLaunch, CommitsFragmentCheck>, KoinComponent {

    private val github: GithubApi by inject()

    private fun getCommit(id: String) = CommitBodyResponse(
        id,
        "https://github.com/square/okhttp/commit/980dfbbe9d0e276b0a103df373f8969e21667e50",
        AuthorResponse(
            "Goooler",
            "https://avatars.githubusercontent.com/u/10363352?v=4",
            "https://github.com/Goooler"
        ),
        CommitResponse(
            10,
            "Update dependencies including kotlin 1.5.31 (#6865)",
            CommitAuthorResponse(
                "2021-09-21T03:37:03.000+08:00",
                "Goooler"
            ),
            CommitCommitterResponse(
                "2021-09-20T20:37:03.000+01:00",
                "GitHub"
            )
        ),
        CommitterResponse(
            "web-flow",
            "https://avatars.githubusercontent.com/u/19864447?v=4",
            "https://github.com/web-flow"
        )
    )

    private val repo = "square/okhttp"
    private val language = "Kotlin"

    private val args = Bundle().apply {
        putString(REPOSITORY_FULL_NAME_ARGS, repo)
        putString(LANGUAGE_REPOSITORY_ARGS, language)
    }

    private val q: String
        get() = "repo:$repo+$language"

    override fun createCheck(): CommitsFragmentCheck {
        return CommitsFragmentCheck()
    }

    override fun createLaunch(): CommitsFragmentLaunch {
        return CommitsFragmentLaunch()
    }

    override fun setupLaunch() {
        launchFragmentInContainer<CommitsFragment>(args, themeResId = R.style.Theme_GithubApi)
        Thread.sleep(500)
    }

    fun withLoadCommitsError() {
        coEvery { github.searchCommitsAsync(q, any(), any()) } throws
                Exception()
    }

    fun withLoadCommitsFirstPage() {
        coEvery { github.searchCommitsAsync(q, 1, any()) } returns
                CommitsResponse(
                    1,
                    listOf(getCommit("1"))
                )
    }

    fun withLoadCommitsTwoPages() {
        coEvery { github.searchCommitsAsync(q, 1, any()) } returns
                CommitsResponse(
                    12,
                    listOf(
                        getCommit("1"),
                        getCommit("2"),
                        getCommit("3"),
                        getCommit("4"),
                        getCommit("5"),
                        getCommit("6"),
                        getCommit("7"),
                        getCommit("8"),
                        getCommit("9"),
                        getCommit("10")
                    )
                )

        coEvery { github.searchCommitsAsync(q, 2, any()) } returns
                CommitsResponse(
                    12,
                    listOf(
                        getCommit("11"),
                        getCommit("12")
                    )
                )
    }

}

class CommitsFragmentLaunch : Launch<CommitsFragmentCheck> {

    override fun createCheck(): CommitsFragmentCheck {
        return CommitsFragmentCheck()
    }

    fun scrollToLastItem() {
        R.id.commits.scrollToPosition(9)
    }

    fun clickAccessCommit() {
        "https://github.com/square/okhttp/commit/980dfbbe9d0e276b0a103df373f8969e21667e50".mockLinkOpening()
        R.id.access.click()
    }

    fun clickTryAgain() {
        R.id.error_button.click()
    }

}

class CommitsFragmentCheck : Check, KoinComponent {

    private val github: GithubApi by inject()
    private val repo = "square/okhttp"
    private val language = "Kotlin"

    private val q: String
        get() = "repo:$repo+$language"

    fun loadCommitsFirstPage() {
        coVerify {
            github.searchCommitsAsync(q, 1, any())
        }
    }

    fun loadCommitsSecondPage() {
        coVerify {
            github.searchCommitsAsync(q, 2, any())
        }
    }

    fun commitsDisplayed() {
        R.id.author.hasText("Author: Goooler")
        R.id.message.hasText("Commit message:\nUpdate dependencies including kotlin 1.5.31 (#6865)")
        R.id.date.hasText("Created: 2021/09/21")
        R.id.access.isDisplayed()
    }

    fun openCommitLink() {
        "https://github.com/square/okhttp/commit/980dfbbe9d0e276b0a103df373f8969e21667e50".checkLinkOpening()
    }

    fun errorFirstPageDisplayed() {
        errorViewDisplayed()
    }

    fun loadCommitsFirstPageTwoTimes() {
        coVerify(exactly = 2) {
            github.searchCommitsAsync(q, 1, any())
        }
    }

}