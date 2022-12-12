package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.di.IoDispatcher
import com.atakanmadanoglu.notesapplication.di.MainDispatcher
import com.atakanmadanoglu.notesapplication.domain.usecases.EditNoteUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.GetNoteByIdUseCase
import com.atakanmadanoglu.notesapplication.presentation.edit_note.navigation.EditNoteArgs
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val editNoteArgs = EditNoteArgs(savedStateHandle)

    private val retrievedData = MutableStateFlow<NoteUI?>(null)

    private val _editNoteUiState = MutableEditNoteUiState()
    val editNoteUiState: EditNoteUiState get() = _editNoteUiState

    init {
        viewModelScope.launch(ioDispatcher) {
            getNoteByIdUseCase.invoke(
                id = editNoteArgs.noteId
            ).collectLatest { noteUI ->
                withContext(mainDispatcher) {
                    retrievedData.value = noteUI
                    with(_editNoteUiState) {
                        title = noteUI.title
                        description = noteUI.description
                        createdAt = noteUI.createdAt
                    }
                }
            }
        }
    }

    fun editNote(
        inputTitle: String,
        inputDescription: String
    ) = viewModelScope.launch(ioDispatcher) {
        editNoteUseCase.invoke(
            id = retrievedData.value?.id ?: -1,
            title = inputTitle,
            description = inputDescription
        )
    }

    fun updateTitleValue(newValue: String) {
        _editNoteUiState.title = newValue
        refreshDoneIconVisibility()
    }

    fun updateDescriptionValue(newValue: String) {
        _editNoteUiState.description = newValue
        refreshDoneIconVisibility()
    }

    private fun refreshDoneIconVisibility() = with(retrievedData.value) {
         _editNoteUiState.isDoneIconVisible = _editNoteUiState.isNewValueEntered(
             retrievedTitle = this?.title ?: "",
             retrievedDescription = this?.description ?: ""
         )
    }

    fun updateRetrievedData() = viewModelScope.launch(ioDispatcher) {
        getNoteByIdUseCase.invoke(
            editNoteArgs.noteId
        ).collectLatest {
            retrievedData.value = it
        }
    }

    fun makeDoneDoneIconInvisible() {
        _editNoteUiState.isDoneIconVisible = false
    }
}