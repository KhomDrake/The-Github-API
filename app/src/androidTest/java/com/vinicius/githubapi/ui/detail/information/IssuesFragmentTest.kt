package com.vinicius.githubapi.ui.detail.information

import com.vinicius.githubapi.utils.rule.IntentsRule
import com.vinicius.githubapi.utils.rule.KoinRule
import org.junit.Rule
import org.junit.Test

class IssuesFragmentTest {
    
    @get:Rule
    val koinRule = KoinRule()
    
    @get:Rule
    val intentsRule = IntentsRule()

    @Test
    fun openFragment_shouldLoadIssuesFirstPage() {
        setup {
            withLoadIssuesFirstPage()
        } check {
            loadIssuesFirstPage()
        }
    }

    @Test
    fun openFragmentWithLoadIssues_andScrollToLastItem_shouldLoadSecondPage() {
        setup {
            withLoadIssuesTwoPages()
        } launch {
            scrollToLastItem()
        } check {
            loadIssuesSecondPage()
        }
    }

    @Test
    fun openFragmentWithLoadIssues_shouldShowIssues() {
        setup {
            withLoadIssuesFirstPage()
        } check {
            issuesDisplayed()
        }
    }

    @Test
    fun openFragmentWithLoadIssues_andClickAccessIssue_shouldOpenIssueLink() {
        setup {
            withLoadIssuesFirstPage()
        } launch {
            clickAccessIssue()
        } check {
            openIssueLink()
        }
    }

    @Test
    fun openFragmentWithLoadIssuesErrorFirstPage_shouldShowErrorFirstPage() {
        setup {
            withLoadIssuesError()
        } check {
            errorFirstPageDisplayed()
        }
    }

    @Test
    fun openFragmentWithLoadIssuesErrorFirstPage_andClickTryAgain_shouldTryToLoadFirstPageAgain() {
        setup {
            withLoadIssuesError()
        } launch {
            clickTryAgain()
        } check {
            loadIssuesFirstPageTwoTimes()
        }
    }
    
}