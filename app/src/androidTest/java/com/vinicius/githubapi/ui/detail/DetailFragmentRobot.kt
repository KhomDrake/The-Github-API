package com.vinicius.githubapi.ui.detail

import androidx.fragment.app.testing.launchFragmentInContainer
import com.vinicius.githubapi.R
import com.vinicius.githubapi.data.network.*
import com.vinicius.githubapi.data.ui.License
import com.vinicius.githubapi.data.ui.OwnerRepository
import com.vinicius.githubapi.data.ui.Repository
import com.vinicius.githubapi.remote.network.GithubApi
import com.vinicius.githubapi.utils.Check
import com.vinicius.githubapi.utils.Launch
import com.vinicius.githubapi.utils.Setup
import com.vinicius.githubapi.utils.extension.checkTextTabLayoutPosition
import com.vinicius.githubapi.utils.extension.clickTabLayoutPosition
import com.vinicius.githubapi.utils.extension.hasText
import com.vinicius.githubapi.utils.extension.isDisplayed
import io.mockk.coEvery
import io.mockk.coVerify
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun DetailFragmentTest.setup(func: DetailFragmentSetup.() -> Unit) =
    DetailFragmentSetup().apply(func)

class DetailFragmentSetup : Setup<DetailFragmentLaunch, DetailFragmentCheck>, KoinComponent {

    private val args = DetailFragmentArgs(
        Repository(
            232,
            "okhttp",
            "square/okhttp",
            OwnerRepository(
                "square",
                323232,
                "https://avatars.githubusercontent.com/u/82592?v=4",
                "Organization",
                "https://api.github.com/users/square"
            ),
            "Square’s meticulous HTTP client for the JVM, Android, and GraalVM.",
            false,
            "2012-07-23T13:42:55Z",
            "2012-07-23T13:42:55Z",
            "master",
            4.7,
            3232,
            32323,
            "Kotlin",
            "40940",
            23232,
            "https://github.com/square/okhttp",
            License(
                "https://api.github.com/licenses/apache-2.0",
                "apache-2.0",
                "Apache License 2.0"
            )
        )
    ).toBundle()

    private val githubApi: GithubApi by inject()

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

    override fun createCheck(): DetailFragmentCheck {
        return DetailFragmentCheck()
    }

    override fun createLaunch(): DetailFragmentLaunch {
        return DetailFragmentLaunch()
    }

    override fun setupLaunch() {
        launchFragmentInContainer<DetailFragment>(args, themeResId = R.style.Theme_GithubApi)
        Thread.sleep(200)
    }

    fun withLoadCommits() {
        coEvery { githubApi.searchCommitsAsync(any(), any(), any()) } returns
                CommitsResponse(
                    1,
                    listOf(getCommit("1"))
                )
    }

    fun withLoadIssues() {
        coEvery {
            githubApi.searchIssuesAsync(
                any(), any(), any()
            )
        } returns IssuesResponse(
            1,
            listOf(
                getIssue(1)
            )
        )
    }

}

class DetailFragmentLaunch : Launch<DetailFragmentCheck> {

    override fun createCheck(): DetailFragmentCheck {
        return DetailFragmentCheck()
    }

    fun clickIssues() {
        R.id.tab_information.clickTabLayoutPosition(1)
    }

    fun clickLicense() {
        R.id.tab_information.clickTabLayoutPosition(2)
    }

    fun clickLinks() {
        R.id.tab_information.clickTabLayoutPosition(3)
    }

}

class DetailFragmentCheck : Check, KoinComponent {

    private val githubApi: GithubApi by inject()

    fun repositoryInformationDisplayed() {
        R.id.name.hasText("okhttp")
        R.id.full_name.hasText("square/okhttp")
        R.id.description.hasText("Square’s meticulous HTTP client for the JVM, Android, and GraalVM.")
        R.id.score.hasText("Score: 4.7")
        R.id.watchers.hasText("Watchers: 3232")
        R.id.open_issues.hasText("Open Issues: 32323")
        R.id.stars.hasText("Stars: 40940")

        R.id.tab_information.checkTextTabLayoutPosition(
            "Commits", 0
        )
        R.id.tab_information.checkTextTabLayoutPosition(
            "Issues", 1
        )
        R.id.tab_information.checkTextTabLayoutPosition(
            "License", 2
        )
        R.id.tab_information.checkTextTabLayoutPosition(
            "Links", 3
        )
    }

    fun loadCommitsCalled() {
        coVerify {
            githubApi.searchCommitsAsync(any(), any(), any())
        }
    }

    fun loadIssuesCalled() {
        coVerify {
            githubApi.searchIssuesAsync(
                any(), any(), any()
            )
        }
    }

    fun licenseDisplayed() {
        "Apache License 2.0".isDisplayed()
    }

    fun linksDisplayed() {
        "https://github.com/square/okhttp".isDisplayed()
        "https://api.github.com/users/square".isDisplayed()
    }

}