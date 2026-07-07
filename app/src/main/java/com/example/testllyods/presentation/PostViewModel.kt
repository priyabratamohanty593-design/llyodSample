package com.example.testllyods.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testllyods.domain.DomainPost
import com.example.testllyods.domain.GetFilteredPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getFilteredPostsUseCase: GetFilteredPostsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostsUiState())
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    fun fetchPosts(minId: Int = 0) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            val users = getFilteredPostsUseCase(minId)
            _uiState.update { it.copy(isLoading = false, users = users) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown") }
        }
    }
}

data class PostsUiState(
    val isLoading: Boolean = false,
    val users: List<DomainPost> = emptyList(),
    val error: String? = null
)
