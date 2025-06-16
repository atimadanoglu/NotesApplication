package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.usecases.DeleteNoteByIdUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.DeleteNotesByIdsUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.GetNotesByCreatedAtUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.SearchNotesUseCase
import com.atakanmadanoglu.notesapplication.presentation.model.NoteListEvent
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import com.atakanmadanoglu.notesapplication.presentation.model.NotesListUIState
import com.atakanmadanoglu.notesapplication.presentation.model.NotesListUiOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListScreenViewModel @Inject constructor(
    private val getNotesByCreatedAtUseCase: GetNotesByCreatedAtUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val deleteNotesByIdsUseCase: DeleteNotesByIdsUseCase,
    private val deleteNoteByIdUseCase: DeleteNoteByIdUseCase
): ViewModel() {

    private val _state = MutableStateFlow(NotesListUIState())
    val state: StateFlow<NotesListUIState> get() = _state

    fun onEvent(event: NoteListEvent) {
        when (event) {
            NoteListEvent.CancelChoosingNote -> cancelNoteSelection()
            NoteListEvent.MakeSearchValueEmpty -> updateSearchValue("")
            NoteListEvent.OnDeleteClicked -> _state.update { it.copy(showDeletionDialog = true) }
            NoteListEvent.OnDeleteOperationApproved -> handleDeletion()
            NoteListEvent.OnDismissRequest -> _state.update { it.copy(showDeletionDialog = false) }
            NoteListEvent.OnSelectAllClicked -> toggleSelectAll()
            is NoteListEvent.OnCardLongClick -> activateSelectionMode(event.noteIndex)
            is NoteListEvent.OnSearchValueChange -> handleSearch(event.newValue)
            is NoteListEvent.OnSwiped -> deleteNote(event.index)
            is NoteListEvent.UpdateCheckboxState -> toggleNoteSelection(event.index)
            else -> {}
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            getNotesByCreatedAtUseCase().collect { notesList ->
                _state.update {
                    // Restore checkbox states for selected notes
                    _state.value.selectedNotesIndexes.forEach { index ->
                        if (index < notesList.size) notesList[index].isChecked = true
                    }
                    it.copy(notes = notesList, totalNotesCount = notesList.size)
                }
            }
        }
    }

    fun getNotes(): List<NoteUI> {
        // Update search state and return appropriate list
        _state.update { it.copy(isSearchValueEntered = it.searchText.isNotEmpty()) }
        return if (_state.value.searchText.isEmpty()) {
            _state.value.notes
        } else {
            _state.value.searchedNotesList
        }
    }

    private fun handleSearch(query: String) {
        _state.update { it.copy(searchText = query) }
        viewModelScope.launch {
            val filteredList = searchNotesUseCase(_state.value.notes, query)
            _state.update { it.copy(searchedNotesList = filteredList) }
        }
    }

    private fun toggleNoteSelection(noteIndex: Int) {
        _state.update {
            val notesList = it.notes.toMutableList()
            val note = notesList[noteIndex]
            note.isChecked = !note.isChecked

            val selectedIndexes = it.selectedNotesIndexes.toMutableList()
            if (note.isChecked) {
                selectedIndexes.add(noteIndex)
            } else {
                selectedIndexes.remove(noteIndex)
            }

            val selectedCount = notesList.count { noteUI -> noteUI.isChecked }
            val allSelected = selectedCount == notesList.size && selectedCount > 0

            it.copy(
                notes = notesList,
                selectedNotesIndexes = selectedIndexes,
                selectedNotesCount = selectedCount,
                selectAllClicked = allSelected
            )
        }
    }

    private fun toggleSelectAll() {
        val shouldSelectAll = !_state.value.selectAllClicked

        _state.update {
            val notesList = it.notes.toMutableList()
            // Update all checkboxes
            notesList.forEach { note -> note.isChecked = shouldSelectAll }

            val selectedIndexes = if (shouldSelectAll) {
                notesList.indices.toList()
            } else {
                emptyList()
            }

            it.copy(
                notes = notesList,
                selectAllClicked = shouldSelectAll,
                selectedNotesIndexes = selectedIndexes,
                selectedNotesCount = if (shouldSelectAll) notesList.size else 0
            )
        }
    }

    private fun activateSelectionMode(noteIndex: Int) {
        _state.update { it.copy(operationType = NotesListUiOperation.SelectNotes) }
        toggleNoteSelection(noteIndex)
    }

    private fun cancelNoteSelection() {
        _state.update {
            val notesList = it.notes.toMutableList()
            // Uncheck all notes
            notesList.forEach { note -> note.isChecked = false }

            it.copy(
                notes = notesList,
                operationType = NotesListUiOperation.DisplayNotes,
                selectedNotesIndexes = emptyList(),
                selectAllClicked = false,
                selectedNotesCount = 0
            )
        }
    }

    private fun handleDeletion() {
        viewModelScope.launch {
            val selectedNotesIds = _state.value.getSelectedNotesIds()
            deleteNotesByIdsUseCase(selectedNotesIds)
        }

        cancelNoteSelection()
        _state.update { it.copy(showDeletionDialog = false) }
    }

    private fun deleteNote(id: Int) {
        viewModelScope.launch {
            deleteNoteByIdUseCase(id)
        }
    }

    private fun updateSearchValue(newValue: String) {
        _state.update { it.copy(searchText = newValue) }
        handleSearch(newValue)
    }
}