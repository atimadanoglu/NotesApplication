package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.usecases.GetNotesByCreatedAtUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.SearchNotesUseCase
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
interface NotesListUiState {
    val allNotesList: SnapshotStateList<NoteUI>
    val searchedNotesList: SnapshotStateList<NoteUI>
    val totalNotesCount: Int
    val searchValue: String
    fun isSearchValueEntered(): Boolean
}

private class MutableNotesListUiState: NotesListUiState {
    override var allNotesList = mutableStateListOf<NoteUI>()
    override var searchedNotesList = mutableStateListOf<NoteUI>()
    override var totalNotesCount by mutableStateOf(allNotesList.size)
    override var searchValue by mutableStateOf("")
    override fun isSearchValueEntered(): Boolean {
        return searchValue.isNotEmpty()
    }
}

@HiltViewModel
class NotesListScreenViewModel @Inject constructor(
    private val getNotesByCreatedAtUseCase: GetNotesByCreatedAtUseCase,
    private val searchNotesUseCase: SearchNotesUseCase
): ViewModel() {
    private val _notesListUiState = MutableNotesListUiState()
    val notesListUiState: NotesListUiState = _notesListUiState

    fun getAllNotes() {
        viewModelScope.launch {
            getNotesByCreatedAtUseCase.invoke().collectLatest {
                with(_notesListUiState) {
                    allNotesList.clear()
                    allNotesList.addAll(it)
                    totalNotesCount = it.size
                }
            }
        }
    }

    fun searchAndGetNotes() = viewModelScope.launch {
        with(_notesListUiState) {
            val filteredList = searchNotesUseCase.invoke(allNotesList, searchValue)
            searchedNotesList.clear()
            searchedNotesList.addAll(filteredList)
        }
    }

    fun decideWhichListWillBeUsed(): List<NoteUI> = with(_notesListUiState) {
        return if (!isSearchValueEntered()) {
            allNotesList
        } else {
            searchedNotesList
        }
    }

    fun setSearchValue(newValue: String) {
        _notesListUiState.searchValue = newValue
    }
}