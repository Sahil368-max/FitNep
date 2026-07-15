package com.example.fitnep.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fitnep.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testLoginUI_InputAndClick() {
        // Type email
        onView(withId(R.id.etEmail))
            .perform(typeText("test@example.com"), closeSoftKeyboard())

        // Type password
        onView(withId(R.id.etPassword))
            .perform(typeText("password123"), closeSoftKeyboard())

        // Check if Login button is displayed and click it
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun testNavigateToRegister() {
        // Click on "Register" link
        onView(withId(R.id.tvRegister)).perform(click())

        // Verify Register screen is displayed by checking for a unique ID
        onView(withId(R.id.etName)).check(matches(isDisplayed()))
    }
}
