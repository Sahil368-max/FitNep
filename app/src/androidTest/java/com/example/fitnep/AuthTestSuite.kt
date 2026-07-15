package com.example.fitnep

import com.example.fitnep.ui.login.LoginActivityTest
import com.example.fitnep.ui.register.RegisterActivityTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * This Suite ensures that tests run in the specific order:
 * 1. RegisterActivityTest (Create account)
 * 2. LoginActivityTest (Log in with account)
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    RegisterActivityTest::class,
    LoginActivityTest::class
)
class AuthTestSuite
