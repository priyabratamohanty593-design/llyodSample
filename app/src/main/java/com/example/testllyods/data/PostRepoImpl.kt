package com.example.testllyods.data

import com.example.testllyods.domain.DomainPost
import com.example.testllyods.domain.PostRepo
import javax.inject.Inject

class PostRepoImpl @Inject constructor(private val apiService : ApiService) : PostRepo {
    override suspend fun getPosts(): List<DomainPost> {
        return apiService.getPosts().map {
            DomainPost(
                id = it.id,
                userId = it.userId,
                title = it.title,
                body = it.body
            )
        }
    }
}