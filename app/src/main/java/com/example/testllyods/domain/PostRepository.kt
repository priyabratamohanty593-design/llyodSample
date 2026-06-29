package com.example.testllyods.domain

import com.example.testllyods.data.PostItem

interface PostRepository {

    suspend fun getPosts(): List<DomainPost>

}