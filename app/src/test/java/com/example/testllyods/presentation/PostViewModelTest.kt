package com.example.testllyods.presentation

import app.cash.turbine.test
import com.example.testllyods.domain.DomainPost
import com.example.testllyods.domain.PostRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

    private val repository: PostRepository = mockk()
    private lateinit var viewModel: PostViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUsers success updates uiState with users`() = runTest {
        val posts = listOf(DomainPost("body", 1, "title", 1))
        coEvery { repository.getPosts() } returns posts

        viewModel = PostViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.users).isEqualTo(posts)
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `loadUsers error updates uiState with error message`() = runTest {
        val errorMessage = "Network Error"
        coEvery { repository.getPosts() } throws RuntimeException(errorMessage)

        viewModel = PostViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isEqualTo(errorMessage)
            assertThat(state.users).isEmpty()
        }
    }
}
