package com.vinicius.githubapi.ui.detail.information

import com.vinicius.githubapi.utils.rule.IntentsRule
import com.vinicius.githubapi.utils.rule.KoinRule
import org.junit.Rule
import org.junit.Test

class CommitsFragmentTest {

    @get:Rule
    val koinRule = KoinRule()

    @get:Rule
    val intentsRule = IntentsRule()

    @Test
    fun openFragment_shouldLoadCommitsFirstPage() {
        setup {
            withLoadCommitsFirstPage()
        } check {
            loadCommitsFirstPage()
        }
    }

    @Test
    fun openFragmentWithLoadCommits_andScrollToLastItem_shouldLoadSecondPage() {
        setup {
            withLoadCommitsTwoPages()
        } launch {
            scrollToLastItem()
        } check {
            loadCommitsSecondPage()
        }
    }

    @Test
    fun openFragmentWithLoadCommits_shouldShowCommits() {
        setup {
            withLoadCommitsFirstPage()
        } check {
            commitsDisplayed()
        }
    }

    @Test
    fun openFragmentWithLoadCommits_andClickAccessCommit_shouldOpenCommitLink() {
        setup {
            withLoadCommitsFirstPage()
        } launch {
            clickAccessCommit()
        } check {
            openCommitLink()
        }
    }

    @Test
    fun openFragmentWithLoadCommitsErrorFirstPage_shouldShowErrorFirstPage() {
        setup {
            withLoadCommitsError()
        } check {
            errorFirstPageDisplayed()
        }
    }

    @Test
    fun openFragmentWithLoadCommitsErrorFirstPage_andClickTryAgain_shouldTryToLoadFirstPageAgain() {
        setup {
            withLoadCommitsError()
        } launch {
            clickTryAgain()
        } check {
            loadCommitsFirstPageTwoTimes()
        }
    }

}