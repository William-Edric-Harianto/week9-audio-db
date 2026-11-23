package com.edric.week9_audio_db.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.edric.week9_audio_db.data.remote.ServiceLocator
import com.edric.week9_audio_db.data.repository.MusicRepository
import com.edric.week9_audio_db.model.ArtistDiscography
import com.edric.week9_audio_db.util.toUserMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val DEFAULT_QUERY = "Coldplay"

data class ArtistUiState(
    val query: String = DEFAULT_QUERY,
    val searchedQuery: String = DEFAULT_QUERY,
    val discography: ArtistDiscography? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class ArtistViewModel(
    private val repository: MusicRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState: StateFlow<ArtistUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        startSearch(DEFAULT_QUERY)
    }

    fun onQueryChange(text: String) {
        _uiState.update { it.copy(query = text) }
    }

    fun onSearch() {
        val trimmed = _uiState.value.query.trim()
        if (trimmed.isBlank()) return
        startSearch(trimmed)
    }

    fun retry() {
        val target = _uiState.value.query.trim().ifBlank { _uiState.value.searchedQuery }
        if (target.isBlank()) return
        startSearch(target)
    }

    private fun startSearch(artistQuery: String) {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                searchedQuery = artistQuery,
                discography = null,
                isLoading = true,
                error = null,
            )
        }
        searchJob = viewModelScope.launch {
            repository.getDiscography(artistQuery)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(discography = result, isLoading = false, error = null)
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, error = throwable.toUserMessage())
                    }
                }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { ArtistViewModel(ServiceLocator.musicRepository) }
        }
    }
}
