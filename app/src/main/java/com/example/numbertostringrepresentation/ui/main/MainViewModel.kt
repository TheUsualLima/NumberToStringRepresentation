package com.example.numbertostringrepresentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.numbertostringrepresentation.domain.numtotextparser.NumToTextRepParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val intToTextRepParser: NumToTextRepParser
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    init {
        stopLoading()
    }

    fun updateInput(input: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(inputText = input)
            }
        }
    }

    fun convertInput() {
        viewModelScope.launch {
            startLoading()
            if (uiState.value.inputText.isNotEmpty()) {
                val longInput = uiState.value.inputText.toLongOrNull()
                val labelText = if (longInput == null) {
                    "Number too large"
                } else {
                    intToTextRepParser.parse(longInput)
                }

                _uiState.update {
                    it.copy(
                        label = labelText
                    )
                }
            }
            stopLoading()
        }
    }

    private fun startLoading() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loading = true)
            }
        }
    }

    private fun stopLoading() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loading = false)
            }
        }
    }
}

data class MainState(
    val loading: Boolean = true,
    val inputText: String = "",
    val label: String = "",
)

class MainViewModelFactory(private val intToTextRepParser: NumToTextRepParser) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NumToTextRepParser::class.java)
            .newInstance(intToTextRepParser)
    }
}
