package com.example.testllyods.data

import retrofit2.http.GET

interface ApiService {

    @GET("todos/1/posts")
    suspend fun getPosts(): List<PostItem>
}