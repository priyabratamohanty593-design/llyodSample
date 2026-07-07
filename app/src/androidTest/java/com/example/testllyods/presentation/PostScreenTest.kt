package com.example.testllyods.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testllyods.domain.DomainPost
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsLoadingIndicator() {
        val state = PostsUiState(isLoading = true)

        composeTestRule.setContent {
            PostScreenContent(state = state, onLoadClick = {})
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessage() {
        val errorMessage = "Network Error"
        val state = PostsUiState(error = errorMessage)

        composeTestRule.setContent {
            PostScreenContent(state = state, onLoadClick = {})
        }

        // Substring handles the "Error: " prefix in the UI
        composeTestRule.onNodeWithText(errorMessage, substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
    }

    @Test
    fun successState_showsListOfPosts() {
        val posts = listOf(
            DomainPost(body = "body 1", id = 1, title = "title 1", userId = 101)
        )
        val state = PostsUiState(users = posts)

        composeTestRule.setContent {
            PostScreenContent(state = state, onLoadClick = {})
        }

        composeTestRule.onNodeWithTag("post_list").assertIsDisplayed()
        composeTestRule.onNodeWithText("title 1", substring = true, ignoreCase = true).assertIsDisplayed()
    }

    @Test
    fun clickLoadButton_triggersOnLoadClick() {
        var clicked = false
        val state = PostsUiState()

        composeTestRule.setContent {
            PostScreenContent(
                state = state,
                onLoadClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("load_button").performClick()
        
        assert(clicked)
    }
}
