package com.example.testllyods.domain

import javax.inject.Inject

class GetPostUseCase @Inject constructor(private val postRepo : PostRepo) {

    suspend operator fun invoke() : List<DomainPost>{
        return postRepo.getPosts()
    }
}