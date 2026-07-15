package com.example.fitnep.ui.register

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
class RegisterActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun testFullRegistrationFlow_TwoSteps() {
        // --- STEP 1: Account Creation ---
        onView(withId(R.id.etName))
            .perform(typeText("Test User"), closeSoftKeyboard())
        onView(withId(R.id.etEmail))
            .perform(typeText("newuser@example.com"), closeSoftKeyboard())
        onView(withId(R.id.etPassword))
            .perform(typeText("securePass123"), closeSoftKeyboard())

        onView(withId(R.id.btnRegister)).perform(click())

        // --- STEP 2: Profile Setup (Wait for visibility toggle) ---
        // Note: Using standard Espresso, if the transition is instant, this will work.
        onView(withId(R.id.etAge))
            .perform(typeText("25"), closeSoftKeyboard())
        onView(withId(R.id.etWeight))
            .perform(typeText("75"), closeSoftKeyboard())

        onView(withId(R.id.btnCompleteSetup))
            .check(matches(isDisplayed()))
            .perform(click())
    }
}
