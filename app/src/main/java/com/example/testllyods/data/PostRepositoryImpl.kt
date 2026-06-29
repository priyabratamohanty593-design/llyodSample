package com.example.testllyods.data

import com.example.testllyods.domain.DomainPost
import com.example.testllyods.domain.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor( private val apiService: ApiService) : PostRepository {
    override suspend fun getPosts(): List<DomainPost> {
        return apiService.getPosts().map { it.toDomain() }
    }

}