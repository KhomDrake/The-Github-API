package com.vinicius.githubapi.ui.search

import androidx.fragment.app.testing.launchFragmentInContainer
import com.vinicius.githubapi.R
import com.vinicius.githubapi.data.network.UserResponse
import com.vinicius.githubapi.data.network.UsersResponse
import com.vinicius.githubapi.remote.network.GithubApi
import com.vinicius.githubapi.utils.Check
import com.vinicius.githubapi.utils.Launch
import com.vinicius.githubapi.utils.Setup
import com.vinicius.githubapi.utils.extension.*
import io.mockk.coEvery
import io.mockk.coVerify
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.Exception

fun SearchFragmentTest.setup(func: SearchFragmentSetup.() -> Unit) =
    SearchFragmentSetup().apply(func)

class SearchFragmentSetup : Setup<SearchFragmentLaunch, SearchFragmentCheck>, KoinComponent {

    private val githubApi: GithubApi by inject()

    override fun createCheck(): SearchFragmentCheck {
        return SearchFragmentCheck()
    }

    override fun createLaunch(): SearchFragmentLaunch {
        return SearchFragmentLaunch()
    }

    override fun setupLaunch() {
        launchFragmentInContainer<SearchFragment>(themeResId = R.style.Theme_GithubApi)
    }

    private fun getUser(id: Int) =
        UserResponse(
            "khom",
            id,
            "https://avatars.githubusercontent.com/u/1550444?v=4",
            "https://github.com/khom",
            "User"
        )

    fun withLoadUsersSuccess() {
        coEvery {
            githubApi.searchUsersAsync("Khom", 1)
        } returns UsersResponse(
            listOf(getUser(1)),
            1
        )
    }

    fun withLoadUsersFailure() {
        coEvery {
            githubApi.searchUsersAsync("Khom", 1)
        } throws Exception()
    }

    fun withLoadUsersTwoPages() {
        coEvery {
            githubApi.searchUsersAsync("Khom", 1)
        } returns UsersResponse(
            listOf(
                getUser(1),
                getUser(2),
                getUser(3),
                getUser(4),
                getUser(5),
                getUser(6),
                getUser(7),
                getUser(8),
                getUser(9),
                getUser(10)
            ),
            12
        )

        coEvery {
            githubApi.searchUsersAsync("Khom", 2)
        } returns UsersResponse(
            listOf(
                getUser(11),
                getUser(12)
            ),
            12
        )
    }

}

class SearchFragmentLaunch : Launch<SearchFragmentCheck> {

    override fun createCheck(): SearchFragmentCheck {
        return SearchFragmentCheck()
    }

    fun typeUser() {
        R.id.search_text.typeText("Khom")
    }

    fun clickSearchIcon() {
        R.id.search_icon.click()
    }

    fun clickTryAgain() {
        R.id.search_icon.clickIgnoreConstraint()
    }

    fun scrollToLastItem() {
        R.id.users.scrollToPosition(9)
    }

    fun clickAccessUserHomeLink() {
        "https://github.com/khom".mockLinkOpening()
        R.id.user_home.click()
    }

}

class SearchFragmentCheck : Check, KoinComponent {

    private val githubApi: GithubApi by inject()

    fun initialStateDisplayed() {
        R.id.search_text.hasHint("Search users by username")
        R.id.search_icon.isNotDisplayed()
    }

    fun displayedUserTextAndIconToSearch() {
        R.id.search_text.hasText("Khom")
        R.id.search_icon.isDisplayed()
    }

    fun usersDisplayed() {
        R.id.login.hasText("khom")
    }

    fun loadUsersCalled() {
        coVerify {
            githubApi.searchUsersAsync("Khom", 1)
        }
    }

    fun errorStateDisplayed() {
        errorViewDisplayed()
    }

    fun loadUsersCalledTwoTimes() {
        coVerify(exactly = 2) {
            githubApi.searchUsersAsync(any(), 1)
        }
    }

    fun loadUsersSecondPageCalled() {
        coVerify {
            githubApi.searchUsersAsync("Khom", 2)
        }
    }

    fun openAccessUserHomeLink() {
        "https://github.com/khom".checkLinkOpening()
    }

}