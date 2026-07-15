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
class RegisterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: AuthRepository
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = RegisterViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `register success returns Success resource`() = runTest {
        val user = User(email = "test@example.com", name = "Test User")
        val password = "password123"
        val expectedUser = user.copy(uid = "123")
        
        whenever(repository.register(user, password)).thenReturn(Resource.Success(expectedUser))

        viewModel.register(user, password)
        
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.registerStatus.value
        assertTrue(result is Resource.Success)
        assertEquals(expectedUser, (result as Resource.Success).data)
    }

    @Test
    fun `register with empty fields returns error`() {
        val user = User(email = "", name = "")
        viewModel.register(user, "")

        val result = viewModel.registerStatus.value
        assertTrue(result is Resource.Error)
        assertEquals("Please fill in all fields", (result as Resource.Error).message)
    }

    @Test
    fun `register failure returns Error resource`() = runTest {
        val user = User(email = "test@example.com", name = "Test User")
        val password = "password123"
        val errorMessage = "Email already in use"
        
        whenever(repository.register(user, password)).thenReturn(Resource.Error(errorMessage))

        viewModel.register(user, password)
        
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.registerStatus.value
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}
