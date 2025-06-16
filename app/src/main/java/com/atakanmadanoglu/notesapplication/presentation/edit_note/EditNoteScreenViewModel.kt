package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.usecases.DeleteNoteByIdUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.EditNoteUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.GetNoteByIdUseCase
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
    private val deleteNoteByIdUseCase: DeleteNoteByIdUseCase
): ViewModel() {

    // it will be used to compare the first retrieved and edited values
    private val retrievedDataFromPreviousPage = MutableStateFlow(NoteUI())

    private val _uiState = MutableStateFlow(EditNoteUiState())
    val uiState: StateFlow<EditNoteUiState> get() = _uiState

    fun onTitleChanged(newInput: String) {
        setTitle(newInput)
        setDoneIconVisible()
    }

    fun onDescriptionChanged(newValue: String) {
        setDescription(newValue)
        setDoneIconVisible()
    }

    fun onDeleteButtonClicked() = setOpenDeleteDialog(true)
    fun onDeletionDismissed() = setOpenDeleteDialog(false)

    fun onDoneIconClicked() {
        editNote(
            inputTitle = _uiState.value.title,
            inputDescription = _uiState.value.description
        )
        // RetrievedData needs to be updated to prevent contradiction
        // between old and new data after user click doneIcon
        retrievedDataFromPreviousPage.update {
            it.copy(
                title = _uiState.value.title,
                description = _uiState.value.description,
                createdAt = _uiState.value.createdAt
            )
        }
        setDoneIconVisible()
    }

    private fun setTitle(newValue: String) {
        _uiState.update { it.copy(title = newValue) }
    }

    private fun setDescription(newValue: String) {
        _uiState.update { it.copy(description = newValue) }
    }

    fun setFocus(newValue: Boolean) {
        _uiState.update { it.copy(isFocused = newValue) }
    }

    private fun setDoneIconVisible() = with(retrievedDataFromPreviousPage.value) {
        _uiState.update {
            it.copy(
                isDoneIconVisible = !title.contentEquals(_uiState.value.title) ||
                        !description.contentEquals(_uiState.value.description)
            )
        }
    }

    private fun setOpenDeleteDialog(value: Boolean) {
        _uiState.update { it.copy(openDeleteDialog = value) }
    }

    fun getNoteById(id: Int) {
        viewModelScope.launch {
            getNoteByIdUseCase.invoke(
                id = id
            ).collect { noteUI ->
                retrievedDataFromPreviousPage.value = noteUI
                _uiState.update {
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

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            deleteNoteByIdUseCase(id)
        }
    }
}