package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.usecases.DeleteNoteByIdUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.EditNoteUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.GetNoteByIdUseCase
import com.atakanmadanoglu.notesapplication.presentation.edit_note.navigation.EditNoteArgs
import com.atakanmadanoglu.notesapplication.presentation.model.EditNoteUiState
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteScreenViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val deleteNoteByIdUseCase: DeleteNoteByIdUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val editNoteArgs = EditNoteArgs(savedStateHandle)
    // it will be used to compare the first retrieved and edited values
    private val retrievedData = MutableStateFlow(NoteUI())

    private val _editNoteUiState = MutableStateFlow(EditNoteUiState())
    val editNoteUiState: StateFlow<EditNoteUiState> get() = _editNoteUiState

    init { getNoteById() }

    private fun getNoteById() {
        viewModelScope.launch {
            getNoteByIdUseCase.invoke(
                id = editNoteArgs.noteId
            ).collect { noteUI ->
                retrievedData.value = noteUI
                _editNoteUiState.update {
                    it.copy(
                        title = noteUI.title,
                        description = noteUI.description,
                        createdAt = noteUI.createdAt
                    )
                }
            }
        }
    }

    fun editNote(
        inputTitle: String,
        inputDescription: String
    ) = viewModelScope.launch {
        editNoteUseCase(
            id = retrievedData.value.id,
            title = inputTitle,
            description = inputDescription
        )
        _editNoteUiState.update { it.copy(isDoneIconVisible = false) }
    }

    fun updateTitleValue(newValue: String) {
        _editNoteUiState.update { it.copy(title = newValue) }
        updateDoneIconVisibility()
    }

    fun updateDescriptionValue(newValue: String) {
        _editNoteUiState.update { it.copy(description = newValue) }
        updateDoneIconVisibility()
    }

    fun updateFocusValue(newValue: Boolean) {
        _editNoteUiState.update { it.copy(isFocused = newValue) }
    }
    private fun updateDoneIconVisibility() = with(retrievedData.value) {
         _editNoteUiState.update {
             val isNewValueEntered = _editNoteUiState.value.isNewValueEntered(
                 retrievedTitle = title,
                 retrievedDescription = description
             )
             it.copy(isDoneIconVisible = isNewValueEntered)
         }
    }

    fun setOpenDeleteDialog(value: Boolean) {
        _editNoteUiState.update { it.copy(openDeleteDialog = value) }
    }

    fun deleteNote() {
        viewModelScope.launch {
            deleteNoteByIdUseCase(editNoteArgs.noteId)
        }
    }
}