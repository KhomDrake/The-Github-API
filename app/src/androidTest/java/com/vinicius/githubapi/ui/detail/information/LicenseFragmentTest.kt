package com.vinicius.githubapi.ui.detail.information

import org.junit.Test

class LicenseFragmentTest {

    @Test
    fun openFragmentWithArguments_shouldShowLicense() {
        setup {
            withArguments()
        } check {
            licenseDisplayed()
        }
    }

    @Test
    fun openFragmentWithoutArguments_shouldShowEmptyState() {
        setup {
            withoutArguments()
        } check {
            emptyStateDisplayed()
        }
    }

}