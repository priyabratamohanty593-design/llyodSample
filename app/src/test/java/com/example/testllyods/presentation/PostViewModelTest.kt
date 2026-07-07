package com.example.testllyods.presentation

import app.cash.turbine.test
import com.example.testllyods.domain.DomainPost
import com.example.testllyods.domain.GetFilteredPostsUseCase
import com.example.testllyods.domain.PostRepository
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

    private val repository: PostRepository = mockk()
    private lateinit var useCase: GetFilteredPostsUseCase
    private lateinit var viewModel: PostViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = GetFilteredPostsUseCase(repository)
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
            assertThat(state.users).isEmpty()
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `fetchPosts success updates uiState with all users when minId is 0`() = runTest {
        val posts = listOf(
            DomainPost("body1", 1, "title1", 1),
            DomainPost("body2", 25, "title2", 1)
        )
        coEvery { repository.getPosts() } returns posts

        viewModel.uiState.test {
            // Initial state
            assertThat(awaitItem().isLoading).isFalse()
            
            viewModel.fetchPosts(0)
            
            // Check loading state
            runCurrent() 
            assertThat(awaitItem().isLoading).isTrue()
            
            // Check success state
            advanceUntilIdle()
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.users).isEqualTo(posts)
        }
    }

    @Test
    fun `fetchPosts success updates uiState with filtered users when minId is 21`() = runTest {
        val allPosts = listOf(
            DomainPost("body1", 1, "title1", 1),
            DomainPost("body2", 25, "title2", 1)
        )
        coEvery { repository.getPosts() } returns allPosts

        viewModel.uiState.test {
            awaitItem() // Initial
            
            viewModel.fetchPosts(21)
            
            runCurrent()
            assertThat(awaitItem().isLoading).isTrue() // Loading
            
            advanceUntilIdle()
            val successState = awaitItem()
            assertThat(successState.users).hasSize(1)
            assertThat(successState.users[0].id).isEqualTo(25)
        }
    }

    @Test
    fun `fetchPosts error updates uiState with error message`() = runTest {
        val errorMessage = "Network Error"
        coEvery { repository.getPosts() } throws RuntimeException(errorMessage)

        viewModel.uiState.test {
            // Initial state
            awaitItem()
            
            viewModel.fetchPosts()
            
            // Check loading state
            runCurrent()
            assertThat(awaitItem().isLoading).isTrue()
            
            // Check error state
            advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.error).isEqualTo(errorMessage)
        }
    }
}
