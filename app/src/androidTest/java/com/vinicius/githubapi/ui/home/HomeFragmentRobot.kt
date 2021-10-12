package com.vinicius.githubapi.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.vinicius.githubapi.R
import com.vinicius.githubapi.data.network.LicenseResponse
import com.vinicius.githubapi.data.network.OwnerRepositoryResponse
import com.vinicius.githubapi.data.network.RepositoriesResponse
import com.vinicius.githubapi.data.network.RepositoryResponse
import com.vinicius.githubapi.data.ui.License
import com.vinicius.githubapi.data.ui.OwnerRepository
import com.vinicius.githubapi.data.ui.Repository
import com.vinicius.githubapi.remote.network.GithubApi
import com.vinicius.githubapi.utils.Check
import com.vinicius.githubapi.utils.Launch
import com.vinicius.githubapi.utils.Setup
import com.vinicius.githubapi.utils.extension.*
import io.mockk.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.Exception

fun HomeFragmentTest.setup(func: HomeFragmentSetup.() -> Unit) =
    HomeFragmentSetup().apply(func)

private fun getRepository(id: Int) =
    RepositoryResponse(
        id,
        "okhttp",
        "square/okhttp",
        OwnerRepositoryResponse(
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
        40940,
        23232,
        "https://github.com/square/okhttp",
        LicenseResponse(
            "https://api.github.com/licenses/apache-2.0",
            "apache-2.0",
            "Apache License 2.0"
        )
    )

class HomeFragmentSetup : Setup<HomeFragmentLaunch, HomeFragmentCheck>, KoinComponent {

    private val navController = mockk<NavController>(relaxed = true)

    private val githubApi: GithubApi by inject()

    override fun createCheck(): HomeFragmentCheck {
        return HomeFragmentCheck(navController)
    }

    override fun createLaunch(): HomeFragmentLaunch {
        return HomeFragmentLaunch(navController)
    }

    override fun setupLaunch() {
        every { navController.navigate(
            HomeFragmentDirections.actionHomeToDetailFragment(
                Repository(getRepository(1))
            )
        ) }
        mockkStatic("androidx.navigation.fragment.NavHostFragment")

        every { NavHostFragment.findNavController(any()) } returns navController

        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_GithubApi).onFragment {
            Navigation.setViewNavController((it as Fragment).requireView(), navController)
        }
    }

    fun withLoadLanguageKotlin() {
        coEvery {
            githubApi.searchReposAsync("language:kotlin", any(), any())
        } returns RepositoriesResponse(
            1,
            listOf(
                getRepository(1)
            )
        )
    }

    fun withLoadRepositoriesFail() {
        coEvery {
            githubApi.searchReposAsync("language:kotlin", any(), any())
        } throws Exception()
    }

    fun withLoadLanguageJava() {
        coEvery {
            githubApi.searchReposAsync("language:java", any(), any())
        } returns RepositoriesResponse(
            1,
            listOf(
                getRepository(1)
            )
        )
    }

    fun withLoadRepositoriesTwoPages() {
        coEvery {
            githubApi.searchReposAsync("language:kotlin", 1, any())
        } returns RepositoriesResponse(
            12,
            listOf(
                getRepository(1),
                getRepository(2),
                getRepository(3),
                getRepository(4),
                getRepository(5),
                getRepository(6),
                getRepository(7),
                getRepository(8),
                getRepository(9),
                getRepository(10)
            )
        )

        coEvery {
            githubApi.searchReposAsync("language:kotlin", 2, any())
        } returns RepositoriesResponse(
            12,
            listOf(
                getRepository(11),
                getRepository(12)
            )
        )
    }

}

class HomeFragmentLaunch(private val navController: NavController) : Launch<HomeFragmentCheck> {

    override fun createCheck(): HomeFragmentCheck {
        return HomeFragmentCheck(navController)
    }

    fun clickTryAgain() {
        R.id.search_icon.clickIgnoreConstraint()
    }

    fun typeLanguageJava() {
        R.id.search_text.typeText("java")
    }

    fun clickIconSearch() {
        R.id.search_icon.click()
    }

    fun scrollToLastRepository() {
        R.id.repos.scrollToPosition(9)
    }

    fun clickFirstRepository() {
        R.id.repos.clickRecyclerViewItemPosition(0)
    }

}

class HomeFragmentCheck(private val navController: NavController) : Check, KoinComponent {

    private val githubApi: GithubApi by inject()

    fun loadLanguageKotlinCalled() {
        R.id.search_text.hasHint("Search Repos by language")
        R.id.search_icon.isNotDisplayed()

        coVerify {
            githubApi.searchReposAsync("language:kotlin", any(), any())
        }
    }

    fun errorStateDisplayed() {
        errorViewDisplayed()
    }

    fun loadRepositoriesTwoTimes() {
        coVerify(exactly = 2) {
            githubApi.searchReposAsync(any(), any(), any())
        }
    }

    fun repositoryDisplayed() {
        R.id.name.hasText("okhttp")
        R.id.author.hasText("Author: square")
        R.id.stars.hasText("Stars: 40940")
    }

    fun displayedJavaAndIconToSearch() {
        R.id.search_text.hasText("java")
        R.id.search_icon.isDisplayed()
    }

    fun loadRepositoriesLanguageJava() {
        coVerify {
            githubApi.searchReposAsync("language:java", any(), any())
        }
    }

    fun loadRepositoriesSecondPageCalled() {
        coVerify {
            githubApi.searchReposAsync("language:kotlin", 2, any())
        }
    }

    fun goToNextScreen() {
        verify { navController.navigate(
            any<NavDirections>()
        ) }
    }

}