package com.example.testllyods.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PostItemTest {

    @Test
    fun `toDomain maps correctly to DomainPost`() {
        val postItem = PostItem(
            body = "test body",
            id = 1,
            title = "test title",
            userId = 101
        )

        val domainPost = postItem.toDomain()

        assertThat(domainPost.body).isEqualTo(postItem.body)
        assertThat(domainPost.id).isEqualTo(postItem.id)
        assertThat(domainPost.title).isEqualTo(postItem.title)
        assertThat(domainPost.userId).isEqualTo(postItem.userId)
    }
}
