package com.example.testllyods.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import junit.framework.TestCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetPostUseCaseTest {
    private lateinit var repository: PostRepo
    private lateinit var getPostsUseCase: GetPostUseCase

    @Before
    fun setUp() {
        repository  = mockk()
        getPostsUseCase = GetPostUseCase(repository)
    }

    @Test
    fun `invoke should return posts from repository`() = runTest {
        // Given
        val expectedPosts = listOf(
            DomainPost(
                id = 1, title = "Android",
                body = "Compose", userId = 1
            ),
            DomainPost(
                id = 2, title = "Kotlin",
                body = "Coroutines", userId = 2
            )
        )
        coEvery { repository.getPosts() } returns expectedPosts
        // When
        val result = getPostsUseCase()
        // Then
        assertEquals(expectedPosts, result)
        coVerify(exactly = 1) {
            repository.getPosts()
        }
       // confirmVerified(repository)
    }

    @Test
    fun `invoke should return empty list when repository returns empty list`() = runTest {
        // Given
        coEvery { repository.getPosts() } returns emptyList()
        // When
        val result = getPostsUseCase()
        // Then
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) {
            repository.getPosts()
        }
    }

    @Test
    fun `invoke should throw exception when repository fails`() = runTest {
        // Given
        coEvery { repository.getPosts() } throws kotlin.RuntimeException("Network Error")
        // When & Then
        val exception = assertFailsWith<RuntimeException> {
            getPostsUseCase()
        }
        assertEquals("Network Error", exception.message)
        coVerify(exactly = 1) {
            repository.getPosts()
        }
    }
}