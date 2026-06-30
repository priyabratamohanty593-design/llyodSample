package com.example.testllyods.data

import com.example.testllyods.domain.DomainPost
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostRepositoryImplTest {

    private val apiService: ApiService = mockk()
    private lateinit var repository: PostRepositoryImpl

    @Before
    fun setUp() {
        repository = PostRepositoryImpl(apiService)
    }

    @Test
    fun `getPosts calls apiService and maps to domain`() = runTest {
        // Given
        val apiPosts = listOf(
            PostItem("body1", 1, "title1", 10),
            PostItem("body2", 2, "title2", 20)
        )
        coEvery { apiService.getPosts() } returns apiPosts

        // When
        val result = repository.getPosts()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(DomainPost("body1", 1, "title1", 10))
        assertThat(result[1]).isEqualTo(DomainPost("body2", 2, "title2", 20))
    }
}
