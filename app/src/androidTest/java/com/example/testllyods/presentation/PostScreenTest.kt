package com.example.testllyods.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.testllyods.domain.DomainPost
import org.junit.Rule
import org.junit.Test

class PostScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsLoadingIndicator() {
        val state = PostUiState(isLoading = true)

        composeTestRule.setContent {
            PostContent(state = state, onButtonClick = {})
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
        composeTestRule.onNodeWithTag("load_button").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessage() {
        val errorMessage = "Timeout Error"
        val state = PostUiState(error = errorMessage)

        composeTestRule.setContent {
            PostContent(state = state, onButtonClick = {})
        }

        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
        composeTestRule.onNodeWithText("Network :  Error : Timeout Error", substring = true).assertIsDisplayed()
    }

    @Test
    fun successState_showsListOfPosts() {
        val posts = listOf(
            DomainPost(body = "body 1", id = 1, title = "title 1", userId = 101),
            DomainPost(body = "body 2", id = 2, title = "title 2", userId = 102)
        )
        val state = PostUiState(posts = posts)

        composeTestRule.setContent {
            PostContent(state = state, onButtonClick = {})
        }

        composeTestRule.onNodeWithTag("post_list").assertIsDisplayed()
        composeTestRule.onNodeWithTag("post_item_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("post_item_2").assertIsDisplayed()
        
        composeTestRule.onNodeWithText(" title : title 1", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(" title : title 2", substring = true).assertIsDisplayed()
    }

    @Test
    fun clickLoadButton_triggersOnButtonClick() {
        var clicked = false
        val state = PostUiState()

        composeTestRule.setContent {
            PostContent(
                state = state,
                onButtonClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("load_button").performClick()
        
        assert(clicked)
    }
}
