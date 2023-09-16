package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.compose.runtime.Immutable
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

@Immutable
@HiltViewModel
class EditNoteScreenViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val deleteNoteByIdUseCase: DeleteNoteByIdUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val editNoteArgs = EditNoteArgs(savedStateHandle)
    // it will be used to compare the first retrieved and edited values
    private val retrievedDataFromPreviousPage = MutableStateFlow(NoteUI())

    private val _editNoteUiState = MutableStateFlow(EditNoteUiState())
    val editNoteUiState: StateFlow<EditNoteUiState> get() = _editNoteUiState

    init { getNoteById() }

    fun onTitleChanged(newInput: String) {
        setTitle(newInput)
        setDoneIconVisible()
    }

    fun onDescriptionChanged(newValue: String) {
        setDescription(newValue)
        setDoneIconVisible()
    }

    fun whenIsFocused() = setFocus(true)
    fun whenNotHaveFocus() = setFocus(false)
    fun onDeleteButtonClicked() = setOpenDeleteDialog(true)
    fun onDeletionDismissed() = setOpenDeleteDialog(false)
    fun onDeletionApproved() = deleteNote()

    fun onDoneIconClicked() {
        editNote(
            inputTitle = _editNoteUiState.value.title,
            inputDescription = _editNoteUiState.value.description
        )
        // RetrievedData needs to be updated to prevent contradiction
        // between old and new data after user click doneIcon
        retrievedDataFromPreviousPage.update {
            it.copy(
                title = _editNoteUiState.value.title,
                description = _editNoteUiState.value.description,
                createdAt = _editNoteUiState.value.createdAt
            )
        }
        setDoneIconVisible()
    }

    private fun setTitle(newValue: String) {
        _editNoteUiState.update { it.copy(title = newValue) }
    }

    private fun setDescription(newValue: String) {
        _editNoteUiState.update { it.copy(description = newValue) }
    }

    private fun setFocus(newValue: Boolean) {
        _editNoteUiState.update { it.copy(isFocused = newValue) }
    }

    private fun setDoneIconVisible() = with(retrievedDataFromPreviousPage.value) {
        if (
            !title.contentEquals(_editNoteUiState.value.title) ||
            !description.contentEquals(_editNoteUiState.value.description)) {
            _editNoteUiState.update { it.copy(isDoneIconVisible = true) }
        } else {
            _editNoteUiState.update { it.copy(isDoneIconVisible = false) }
        }
    }

    private fun setOpenDeleteDialog(value: Boolean) {
        _editNoteUiState.update { it.copy(openDeleteDialog = value) }
    }

    private fun getNoteById() {
        viewModelScope.launch {
            getNoteByIdUseCase.invoke(
                id = editNoteArgs.noteId
            ).collect { noteUI ->
                retrievedDataFromPreviousPage.value = noteUI
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

    private fun editNote(
        inputTitle: String,
        inputDescription: String
    ) {
        viewModelScope.launch {
            editNoteUseCase(
                id = retrievedDataFromPreviousPage.value.id,
                title = inputTitle,
                description = inputDescription
            )
        }
    }

    private fun deleteNote() {
        viewModelScope.launch {
            deleteNoteByIdUseCase(editNoteArgs.noteId)
        }
    }
}