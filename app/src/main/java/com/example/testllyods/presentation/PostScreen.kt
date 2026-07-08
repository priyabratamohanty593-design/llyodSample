package com.example.testllyods.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testllyods.domain.DomainPost

@Composable
fun PostScreen(viewModel : PostViewModel = hiltViewModel()){
    val state  by viewModel.uiState.collectAsState()

    PostContent(
        state = state,
        onButtonClick = {viewModel.fetchdata()}
    )
}

@Composable
fun PostContent(state: PostUiState, onButtonClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()){
        Button(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .testTag("load_button"),
            onClick = onButtonClick
        ) {
            Text(" load button")
        }
        Box(modifier = Modifier.weight(1f)){
            when {
                state.isLoading ->
                    Box(modifier = Modifier.fillMaxSize()){
                        CircularProgressIndicator(
                            modifier = Modifier.testTag("loading_indicator")
                        )
                    }

                state.error != null ->{
                    Box(modifier = Modifier.fillMaxSize()){
                        Text(
                            text = "Network : ${state.error}",
                            modifier = Modifier.testTag("error_message")
                        )
                    }

                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize().testTag("post_list")
                ) {
                    items(items = state.posts, key = { it.id}){ post ->
                        PostItemUi(post)
                    }
                }
            }
        }
    }
}

@Composable
fun PostItemUi(post: DomainPost) {
    Column(modifier = Modifier.fillMaxWidth().testTag("post_item_${post.id}")){
        Text(text = " id : ${post.id}")
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = " userId : ${post.userId}")
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = " body : ${post.body}")
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = " title : ${post.title}")
        Spacer(modifier = Modifier.height(5.dp))
    }
}