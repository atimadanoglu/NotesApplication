package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.usecases.EditNoteUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.GetNoteByIdUseCase
import com.atakanmadanoglu.notesapplication.presentation.edit_note.navigation.EditNoteArgs
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
interface EditNoteUiState {
    val title: String
    val description: String
    val createdAt: String
    val isDoneIconClicked: Boolean
    val isDoneIconVisible: Boolean
    fun isNewValueEntered(
        retrievedTitle: String,
        retrievedDescription: String
    ): Boolean
}

private class MutableEditNoteUiState: EditNoteUiState {
    override var title by mutableStateOf("")
    override var description by mutableStateOf("")
    override var createdAt by mutableStateOf("")
    override var isDoneIconClicked by mutableStateOf(false)
    override var isDoneIconVisible by mutableStateOf(false)
    override fun isNewValueEntered(
        retrievedTitle: String,
        retrievedDescription: String
    ): Boolean {
        return !title.contentEquals(retrievedTitle) ||
                !description.contentEquals(retrievedDescription)
    }
}

@HiltViewModel
class EditNoteScreenViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val editNoteArgs = EditNoteArgs(savedStateHandle)

    // it will be used to compare the first retrieved and edited values
    private val retrievedData = MutableStateFlow<NoteUI?>(null)

    private val _editNoteUiState = MutableEditNoteUiState()
    val editNoteUiState: EditNoteUiState get() = _editNoteUiState

    fun getNoteById() {
        viewModelScope.launch {
            getNoteByIdUseCase.invoke(
                id = editNoteArgs.noteId
            ).collectLatest { noteUI ->
                retrievedData.value = noteUI
                with(_editNoteUiState) {
                    title = noteUI.title
                    description = noteUI.description
                    createdAt = noteUI.createdAt
                }
            }
        }
    }

    fun editNote(
        inputTitle: String,
        inputDescription: String
    ) = viewModelScope.launch {
        editNoteUseCase.invoke(
            id = retrievedData.value?.id ?: -1,
            title = inputTitle,
            description = inputDescription
        )
        _editNoteUiState.isDoneIconVisible = false
    }

    fun updateTitleValue(newValue: String) {
        _editNoteUiState.title = newValue
        updateDoneIconVisibility()
    }

    fun updateDescriptionValue(newValue: String) {
        _editNoteUiState.description = newValue
        updateDoneIconVisibility()
    }

    private fun updateDoneIconVisibility() = with(retrievedData.value) {
         _editNoteUiState.isDoneIconVisible = _editNoteUiState.isNewValueEntered(
             retrievedTitle = this?.title ?: "",
             retrievedDescription = this?.description ?: ""
         )
    }
}