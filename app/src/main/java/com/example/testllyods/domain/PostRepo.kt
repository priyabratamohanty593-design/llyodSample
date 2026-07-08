package com.example.testllyods.domain

interface PostRepo {

    suspend fun getPosts():List<DomainPost>
}