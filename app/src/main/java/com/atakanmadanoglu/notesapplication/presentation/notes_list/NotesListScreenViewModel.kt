package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.di.DefaultDispatcher
import com.atakanmadanoglu.notesapplication.domain.GetNotesUseCase
import com.atakanmadanoglu.notesapplication.domain.model.NoteUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
interface NotesListUiState {
    val allNotesList: SnapshotStateList<NoteUI>
    val searchedNotesList: SnapshotStateList<NoteUI>
    val totalNotesCount: Int
    val searchValue: String
}

private class MutableNotesListUiState: NotesListUiState {
    override var allNotesList = mutableStateListOf<NoteUI>()
    override var searchedNotesList = mutableStateListOf<NoteUI>()
    override var totalNotesCount by mutableStateOf(allNotesList.size)
    override var searchValue by mutableStateOf("")
}

@HiltViewModel
class NotesListScreenViewModel @Inject constructor(
    getNotesUseCase: GetNotesUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
): ViewModel() {
    private val _notesListUiState = MutableStateFlow(MutableNotesListUiState())
    val notesListUiState: StateFlow<NotesListUiState> = _notesListUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getNotesUseCase.invoke().collectLatest {
                with(_notesListUiState.value) {
                    allNotesList.clear()
                    allNotesList.addAll(it)
                    totalNotesCount = it.size
                }
            }
        }
    }

    fun searchAndGetNotes(
        searchText: String
    ) = viewModelScope.launch(defaultDispatcher) {
        with(_notesListUiState.value) {
            searchedNotesList = allNotesList
            searchedNotesList.filter { noteUI ->
                noteUI.title.contains(searchText, ignoreCase = true) ||
                        noteUI.description.contains(searchText, ignoreCase = true)
            }
            searchValue = searchText
        }
    }

    private val isSearchValueEmpty = MutableStateFlow(_notesListUiState.value.searchValue.isEmpty())

    fun decideWhichListWillBeUsed(): List<NoteUI> = with(_notesListUiState.value) {
        return if (isSearchValueEmpty.value) {
            allNotesList
        } else {
            searchedNotesList
        }
    }

    fun updateSearchStateValue(newValue: String) {
        _notesListUiState.update { currentState ->
            currentState.searchValue = newValue
            currentState
        }
    }
}