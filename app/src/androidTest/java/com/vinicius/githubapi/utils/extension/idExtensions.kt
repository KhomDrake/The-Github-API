package com.vinicius.githubapi.utils.extension

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not

fun Int.hasText(text: String) {
    onView(withId(this)).check(matches(withText(text)))
}

fun Int.isDisplayed() {
    onView(withId(this)).check(matches(ViewMatchers.isDisplayed()))
}

fun Int.isNotDisplayed() {
    onView(withId(this)).check(matches(not(ViewMatchers.isDisplayed())))
}

fun Int.click() {
    onView(withId(this)).perform(ViewActions.click())
}

fun Int.scrollToPosition(position: Int) {
    onView(withId(this)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
}