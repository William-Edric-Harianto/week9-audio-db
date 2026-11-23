package com.edric.week9_audio_db.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.edric.week9_audio_db.data.remote.ServiceLocator
import com.edric.week9_audio_db.data.repository.MusicRepository
import com.edric.week9_audio_db.model.AlbumDetail
import com.edric.week9_audio_db.navigation.AlbumDetailRoute
import com.edric.week9_audio_db.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AlbumDetailUiState {
    data object Loading : AlbumDetailUiState
    data class Success(val album: AlbumDetail) : AlbumDetailUiState
    data class Error(val message: String) : AlbumDetailUiState
}

class AlbumDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MusicRepository,
) : ViewModel() {

    private val albumId: String = savedStateHandle.toRoute<AlbumDetailRoute>().albumId

    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = AlbumDetailUiState.Loading
        viewModelScope.launch {
            repository.getAlbumDetail(albumId)
                .onSuccess { album ->
                    _uiState.value = AlbumDetailUiState.Success(album)
                }
                .onFailure { throwable ->
                    _uiState.value = AlbumDetailUiState.Error(throwable.toUserMessage())
                }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                AlbumDetailViewModel(createSavedStateHandle(), ServiceLocator.musicRepository)
            }
        }
    }
}
