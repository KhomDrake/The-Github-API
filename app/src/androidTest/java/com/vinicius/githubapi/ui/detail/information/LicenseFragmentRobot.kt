package com.vinicius.githubapi.ui.detail.information

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import com.vinicius.githubapi.R
import com.vinicius.githubapi.data.ui.License
import com.vinicius.githubapi.ui.detail.LICENSE_ARGS
import com.vinicius.githubapi.utils.Check
import com.vinicius.githubapi.utils.Launch
import com.vinicius.githubapi.utils.Setup
import com.vinicius.githubapi.utils.extension.hasText
import com.vinicius.githubapi.utils.extension.isDisplayed
import com.vinicius.githubapi.utils.extension.isNotDisplayed

fun LicenseFragmentTest.setup(func: LicenseFragmentSetup.() -> Unit) =
    LicenseFragmentSetup().apply(func)

class LicenseFragmentSetup : Setup<LicenseFragmentLaunch, LicenseFragmentCheck> {

    private var args = Bundle()

    override fun createCheck(): LicenseFragmentCheck {
        return LicenseFragmentCheck()
    }

    override fun createLaunch(): LicenseFragmentLaunch {
        return LicenseFragmentLaunch()
    }

    override fun setupLaunch() {
        launchFragmentInContainer<LicenseFragment>(args, themeResId = R.style.Theme_GithubApi)
    }

    fun withArguments() {
        args.apply {
            putParcelable(LICENSE_ARGS, License(
                "", "", "Apache License 2.0"
            ))
        }
    }

    fun withoutArguments() {
        args = Bundle()
    }
}

class LicenseFragmentLaunch : Launch<LicenseFragmentCheck> {
    override fun createCheck(): LicenseFragmentCheck {
        return LicenseFragmentCheck()
    }

}

class LicenseFragmentCheck : Check {
    fun licenseDisplayed() {
        R.id.license_name.hasText("Apache License 2.0")
        R.id.license_empty.isNotDisplayed()
    }

    fun emptyStateDisplayed() {
        R.id.license_empty.apply {
            hasText("The repository don't have a license")
            isDisplayed()
        }
    }

}