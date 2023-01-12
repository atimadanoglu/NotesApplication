package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.di.DefaultDispatcher
import com.atakanmadanoglu.notesapplication.di.IoDispatcher
import com.atakanmadanoglu.notesapplication.di.MainDispatcher
import com.atakanmadanoglu.notesapplication.domain.usecases.GetNotesByCreatedAtUseCase
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    getNotesByCreatedAtUseCase: GetNotesByCreatedAtUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
): ViewModel() {
    private val _notesListUiState = MutableNotesListUiState()
    val notesListUiState: NotesListUiState = _notesListUiState

    init {
        viewModelScope.launch(ioDispatcher) {
            getNotesByCreatedAtUseCase.invoke().collectLatest {
                withContext(mainDispatcher) {
                    with(_notesListUiState) {
                        allNotesList.clear()
                        allNotesList.addAll(it)
                        totalNotesCount = it.size
                    }
                }
            }
        }
    }

    fun searchAndGetNotes(
        searchText: String
    ) = viewModelScope.launch {
        with(_notesListUiState) {
            searchedNotesList.clear()
            val filteredList = withContext(defaultDispatcher) {
                allNotesList.filter { noteUI ->
                    noteUI.title.contains(searchText, ignoreCase = true) ||
                            noteUI.description.contains(searchText, ignoreCase = true)
                }
            }
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