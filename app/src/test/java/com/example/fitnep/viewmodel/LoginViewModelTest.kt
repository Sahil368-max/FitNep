package com.example.fitnep.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fitnep.data.model.User
import com.example.fitnep.data.repository.AuthRepository
import com.example.fitnep.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: AuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = LoginViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success returns Success resource`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val expectedUser = User(uid = "123", email = email, name = "Test User")
        
        whenever(repository.login(email, password)).thenReturn(Resource.Success(expectedUser))

        viewModel.login(email, password)
        
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.loginStatus.value
        assertTrue(result is Resource.Success)
        assertEquals(expectedUser, (result as Resource.Success).data)
    }

    @Test
    fun `login with empty fields returns error`() {
        viewModel.login("", "")

        val result = viewModel.loginStatus.value
        assertTrue(result is Resource.Error)
        assertEquals("Email and password cannot be empty", (result as Resource.Error).message)
    }

    @Test
    fun `login failure returns Error resource`() = runTest {
        val email = "test@example.com"
        val password = "wrong_password"
        val errorMessage = "Invalid credentials"
        
        whenever(repository.login(email, password)).thenReturn(Resource.Error(errorMessage))

        viewModel.login(email, password)
        
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.loginStatus.value
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}
