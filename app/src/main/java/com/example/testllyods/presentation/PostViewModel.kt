package com.example.testllyods.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testllyods.domain.DomainPost
import com.example.testllyods.domain.GetPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val postUseCase : GetPostUseCase)
    : ViewModel(){

        private val _uiState = MutableStateFlow(PostUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchdata() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true,error = null) }
        try{
            val filterPost = postUseCase()
            _uiState.update { it.copy(isLoading = false , posts = filterPost) }
        }catch (e: Exception){
            _uiState.update { it.copy(isLoading = false,error = " Error : ${e.message}") }
        }
    }

}

data class PostUiState(
    val isLoading : Boolean = false,
    val error : String? = null,
    val posts : List<DomainPost> = emptyList()
)