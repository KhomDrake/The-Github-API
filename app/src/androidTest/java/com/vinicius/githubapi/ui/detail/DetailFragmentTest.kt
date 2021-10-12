package com.vinicius.githubapi.ui.detail

import com.vinicius.githubapi.utils.rule.KoinRule
import org.junit.Rule
import org.junit.Test

class DetailFragmentTest {

    @get:Rule
    val koinRule = KoinRule()

    @Test
    fun openScreen_shouldShowRepositoryInformation() {
        setup {

        } check {
            repositoryInformationDisplayed()
        }
    }

    @Test
    fun openScreen_shouldLoadCommits() {
        setup {
            withLoadCommits()
        } check {
            loadCommitsCalled()
        }
    }

    @Test
    fun openScreen_andClickIssues_shouldLoadIssues() {
        setup {
            withLoadCommits()
            withLoadIssues()
        } launch {
            clickIssues()
            Thread.sleep(200)
        } check {
            loadIssuesCalled()
        }
    }

    @Test
    fun openScreen_andClickLicense_shouldShowLicense() {
        setup {
            withLoadCommits()
        } launch {
            clickLicense()
        } check {
            licenseDisplayed()
        }
    }

    @Test
    fun openScreen_andClickLinks_shouldShowLinks() {
        setup {
            withLoadCommits()
        } launch {
            clickLinks()
        } check {
            linksDisplayed()
        }
    }

}