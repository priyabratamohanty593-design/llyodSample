package com.example.testllyods.data

import com.example.testllyods.domain.DomainPost

data class PostItem(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
){

    fun toDomain() = DomainPost(
        body = body,
        id = id,
        title = title,
        userId = userId
    )
}