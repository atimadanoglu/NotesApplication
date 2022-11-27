package com.atakanmadanoglu.notesapplication.presentation.add_note

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.AddNoteUseCase
import com.atakanmadanoglu.notesapplication.presentation.mapper.AddNoteRequestMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@Stable
interface AddNoteUiState {
    val createdAt: Long
    val title: String
    val description: String

    fun isBothTitleAndDescriptionEntered(): Boolean
}

private class MutableAddNoteUiState: AddNoteUiState {
    override var createdAt by mutableStateOf(0L)
    override var title by mutableStateOf("")
    override var description by mutableStateOf("")

    override fun isBothTitleAndDescriptionEntered(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }
}

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    private val addNoteRequestMapper: AddNoteRequestMapper
): ViewModel() {

    private val _addNoteUiState = MutableStateFlow(MutableAddNoteUiState())
    val addNoteUiState: StateFlow<AddNoteUiState> = _addNoteUiState.asStateFlow()

    fun addNote(
        inputTitle: String,
        inputDescription: String
    ) = viewModelScope.launch {
        with(_addNoteUiState.value) {
            createdAt = Date().time
            title = inputTitle
            description = inputDescription
            addNoteUseCase.invoke(
                addNoteRequestMapper.mapToAddNoteRequest(this)
            )
        }
    }

    fun updateTitleStateValue(newValue: String) {
        _addNoteUiState.update { currentState ->
            currentState.title = newValue
            currentState
        }
    }

    fun updateDescriptionStateValue(newValue: String) {
        _addNoteUiState.update { currentState ->
            currentState.description = newValue
            currentState
        }
    }
}