package com.vinicius.githubapi.ui.home

import com.vinicius.githubapi.utils.rule.KoinRule
import org.junit.Rule
import org.junit.Test

class HomeFragmentTest {

    @get:Rule
    val koinRule = KoinRule()

    @Test
    fun openScreen_shouldLoadLanguageKotlin() {
        setup {
            withLoadLanguageKotlin()
        }  check {
            loadLanguageKotlinCalled()
        }
    }

    @Test
    fun openScreenWithLoadRepositoriesFail_shouldShowFirstPageError() {
        setup {
            withLoadRepositoriesFail()
        } check {
            errorStateDisplayed()
        }
    }

    @Test
    fun openScreenWithLoadRepositoriesFail_andClickTryAgain_shouldLoadRepositoriesAgain() {
        setup {
            withLoadRepositoriesFail()
        } launch {
            clickTryAgain()
        } check {
            loadRepositoriesTwoTimes()
        }
    }

    @Test
    fun openScreen_shouldShowRepository() {
        setup {
            withLoadLanguageKotlin()
        } check {
            repositoryDisplayed()
        }
    }

    @Test
    fun openScreen_andClickFirstRepository_shouldGoToNextScreen() {
        setup {
            withLoadLanguageKotlin()
        } launch {
            Thread.sleep(300)
            clickFirstRepository()
        } check {
            goToNextScreen()
        }
    }

    @Test
    fun openScreen_andTypeNewLanguage_shouldShowTextLanguageAndIconToSearch() {
        setup {
            withLoadLanguageKotlin()
            withLoadLanguageJava()
        } launch {
            typeLanguageJava()
        } check {
            displayedJavaAndIconToSearch()
        }
    }

    @Test
    fun openScreen_andTypeNewLanguage_andClickSearchIcon_shouldLoadRepositoriesNewLanguage() {
        setup {
            withLoadLanguageKotlin()
            withLoadLanguageJava()
        } launch {
            typeLanguageJava()
            clickIconSearch()
        } check {
            loadRepositoriesLanguageJava()
        }
    }

    @Test
    fun openScreen_andScrollToLastRepository_shouldLoadSecondPage() {
        setup {
            withLoadRepositoriesTwoPages()
        } launch {
            scrollToLastRepository()
        } check {
            loadRepositoriesSecondPageCalled()
        }
    }

}