package com.example.appstarterkit.ui.screen

import com.example.appstarterkit.domain.usecase.FetchExamplesUseCase
import com.example.appstarterkit.domain.usecase.GetExamplesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * UI State for Example Screen
 */
sealed class ExampleUiState {
    object Loading : ExampleUiState()
    data class Success(val examples: List<com.example.appstarterkit.domain.model.Example>) : ExampleUiState()
    data class Error(val message: String) : ExampleUiState()
}

/**
 * ViewModel for Example Screen
 */
@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getExamplesUseCase: GetExamplesUseCase,
    private val fetchExamplesUseCase: FetchExamplesUseCase
) : BaseViewModel<ExampleUiState>() {

    init {
        observeExamples()
        refreshExamples()
    }

    override fun createInitialState(): ExampleUiState = ExampleUiState.Loading

    private fun observeExamples() {
        launchViewModelScope(
            onError = { e ->
                updateState {
                    ExampleUiState.Error(e.message ?: "Unknown error")
                }
            }
        ) {
            getExamplesUseCase(Unit).collect { result ->
                result.onSuccess { examples ->
                    updateState {
                        ExampleUiState.Success(examples)
                    }
                }.onFailure { e ->
                    updateState {
                        ExampleUiState.Error(e.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun refreshExamples() {
        updateState { ExampleUiState.Loading }
        launchViewModelScope(
            onError = { e ->
                updateState {
                    ExampleUiState.Error(e.message ?: "Unknown error")
                }
            }
        ) {
            val result = fetchExamplesUseCase(
                FetchExamplesUseCase.Params(page = 1, limit = 20)
            )
            result.onSuccess { examples ->
                updateState {
                    ExampleUiState.Success(examples)
                }
            }.onFailure { e ->
                updateState {
                    ExampleUiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
}
