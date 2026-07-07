package com.example.testllyods.domain

import javax.inject.Inject

class GetFilteredPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(minId: Int = 0): List<DomainPost> {
        return repository.getPosts().filter { it.id >= minId }
    }
}
