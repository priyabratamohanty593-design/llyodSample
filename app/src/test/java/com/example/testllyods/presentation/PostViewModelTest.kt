package com.example.testllyods.presentation

import app.cash.turbine.test
import com.example.testllyods.domain.DomainPost
import com.example.testllyods.domain.GetPostUseCase
import com.example.testllyods.domain.PostRepo
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

   // private val repository: PostRepo = mockk()
    private lateinit var useCase: GetPostUseCase
    private lateinit var viewModel: PostViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        useCase  = mockk()
        Dispatchers.setMain(testDispatcher)
       // useCase = GetPostUseCase(repository)
        viewModel = PostViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.posts).isEmpty()
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `fetchPosts success updates uiState with all users`() = runTest {
        val posts = listOf(
            DomainPost("body1", 1, "title1", 1),
            DomainPost("body2", 25, "title2", 1)
        )
        coEvery { useCase() } returns posts

        viewModel.uiState.test {
            // Initial state
            assertThat(awaitItem().isLoading).isFalse()
            
            viewModel.fetchdata()
            
            // Check loading state
            runCurrent() 
            assertThat(awaitItem().isLoading).isTrue()
            
            // Check success state
            advanceUntilIdle()
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.posts).isEqualTo(posts)
        }
    }

    @Test
    fun `fetchPosts error updates uiState with error message`() = runTest {
        val errorMessage = "Network Error"
        coEvery { useCase() } throws RuntimeException(errorMessage)

        viewModel.uiState.test {
            // Initial state
            awaitItem()
            
            viewModel.fetchdata()
            
            // Check loading state
            runCurrent()
            assertThat(awaitItem().isLoading).isTrue()
            
            // Check error state
            advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.error).contains(errorMessage)
        }
    }
}
