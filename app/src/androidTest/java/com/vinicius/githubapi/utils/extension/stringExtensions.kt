package com.vinicius.githubapi.utils.extension

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vinicius.githubapi.R
import org.hamcrest.Matchers

fun String.isDisplayed() {
    onView(withText(this)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

fun errorViewDisplayed() {
    R.id.error_title.hasText("An error has occurred")
    R.id.error_text.hasText("It was not possible to load the information")
    R.id.error_button.hasText("Try Again")
}

fun String.mockLinkOpening() {
    Intents.intending(
        Matchers.allOf(
            IntentMatchers.hasAction(Intent.ACTION_VIEW),
            IntentMatchers.hasData(Uri.parse(this))
        )
    ).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))
}

fun String.checkLinkOpening() {
    Intents.intended(
        Matchers.allOf(
            IntentMatchers.hasAction(Intent.ACTION_VIEW),
            IntentMatchers.hasData(Uri.parse(this))
        )
    )
}