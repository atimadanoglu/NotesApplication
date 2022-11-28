package com.atakanmadanoglu.notesapplication.presentation.add_note

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.usecases.AddNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    private val addNoteUseCase: AddNoteUseCase
): ViewModel() {

    private val _addNoteUiState = MutableAddNoteUiState()
    val addNoteUiState: AddNoteUiState = _addNoteUiState

    fun addNote(
        inputTitle: String,
        inputDescription: String
    ) = viewModelScope.launch {
        addNoteUseCase.invoke(
            inputTitle = inputTitle,
            inputDescription = inputDescription
        )
    }

    fun updateTitleStateValue(newValue: String) {
        _addNoteUiState.title = newValue
    }

    fun updateDescriptionStateValue(newValue: String) {
        _addNoteUiState.description = newValue
    }
}