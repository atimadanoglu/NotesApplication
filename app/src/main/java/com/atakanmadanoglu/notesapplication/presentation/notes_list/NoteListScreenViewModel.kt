package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.usecases.DeleteNoteByIdUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.DeleteNotesByIdsUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.GetNotesByCreatedAtUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.SearchNotesUseCase
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
            NoteListEvent.CancelChoosingNote -> onCancelButtonClicked()
            NoteListEvent.MakeSearchValueEmpty -> setSearchValue("")
            NoteListEvent.OnDeleteClicked -> onDeleteButtonClicked()
            NoteListEvent.OnDeleteOperationApproved -> onDeletionApproved()
            NoteListEvent.OnDismissRequest -> onDeletionDismissed()
            NoteListEvent.OnSelectAllClicked -> onSelectAllClicked()
            is NoteListEvent.OnCardLongClick -> onNoteCardLongPressed(event.noteIndex)
            is NoteListEvent.OnSearchValueChange -> onSearchValueChange(event.newValue)
            is NoteListEvent.OnSwiped -> deleteNote(event.index)
            is NoteListEvent.UpdateCheckboxState -> onCheckboxClicked(event.index)
            else -> {}
        }
    }

    private fun onSearchValueChange(newValue: String) {
        setSearchValue(newValue = newValue)
        searchAndGetNotes()
    }

    private fun onDeletionApproved() {
        deleteNotes()
        setShowDeletionDialog(showDialog = false)
        setOperationType(NotesListUiOperation.DisplayNotes)
        setAllNotesCheckboxUnchecked()
        setSelectedNotesIndexes(indexes = emptyList())
        setSelectAll(false)
    }

    private fun onDeletionDismissed() {
        setShowDeletionDialog(showDialog = false)
    }

    private fun onDeleteButtonClicked() {
        setShowDeletionDialog(showDialog = true)
    }
    fun onCheckboxClicked(noteIndex: Int) {
        setNoteCheckedState(noteIndex)
        setSelectedNotesCount()
        // each time user changes the state of checkbox, synchronise the value of allSelected
        setSelectAll(_state.value.isAllSelected())
    }

    private fun onNoteCardLongPressed(noteIndex: Int) {
        _state.update {
            it.copy(operationType = NotesListUiOperation.SelectNotes)
        }
        onCheckboxClicked(noteIndex)
    }

    private fun onCancelButtonClicked() {
        _state.update {
            setAllNotesCheckboxUnchecked()
            it.copy(
                operationType = NotesListUiOperation.DisplayNotes,
                selectedNotesIndexes = emptyList(),
                selectAllClicked = false
            )
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            getNotesByCreatedAtUseCase().collect { list ->
                _state.update {
                    makeNotesChecked(list)
                    it.copy(
                        notes = list,
                        totalNotesCount = list.size
                    )
                }
            }
        }
    }

    private fun setSearchValueEntered() {
        _state.update { it.copy(isSearchValueEntered = it.searchText.isNotEmpty()) }
    }

    private fun setSelectedNotesCount() {
        _state.update {
            val selectedNotesCount = it.notes.count { noteUI -> noteUI.isChecked }
            it.copy(selectedNotesCount = selectedNotesCount)
        }
    }

    private fun makeNotesChecked(list: List<NoteUI>) {
        _state.value.selectedNotesIndexes.forEach {index ->
            list[index].isChecked = true
        }
    }

    private fun setSelectedNotesIndexes(indexes: List<Int>) {
        _state.update { it.copy(selectedNotesIndexes = indexes) }
    }

    private fun searchAndGetNotes() = viewModelScope.launch {
        _state.update {
            val filteredList = searchNotesUseCase(it.notes, it.searchText)
            it.copy(searchedNotesList = filteredList)
        }
    }

    fun getNotes(): List<NoteUI> = with(_state.value) {
        setSearchValueEntered()
        return if (!isSearchValueEntered) {
            notes
        } else {
            searchedNotesList
        }
    }

    private fun setSearchValue(newValue: String) {
        _state.update { it.copy(searchText = newValue) }
    }

    private fun setAllNotesCheckboxUnchecked() {
        _state.update {
            it.notes.map { noteUI -> noteUI.isChecked = false }
            it.copy(notes = it.notes)
        }
        setSelectedNotesCount()
    }

    private fun setAllNotesCheckboxChecked() {
        _state.update {
            it.notes.map { noteUI -> noteUI.isChecked = true }
            it.copy(notes = it.notes)
        }
        setSelectedNotesCount()
    }

    private fun setShowDeletionDialog(showDialog: Boolean) {
        _state.update { it.copy(showDeletionDialog = showDialog) }
    }

    private fun setNoteCheckedState(noteIndex: Int) {
        _state.update {
            with(it) {
                notes[noteIndex].apply { isChecked = !isChecked }
                val selectedIndexes = selectedNotesIndexes.toMutableList()

                if (notes[noteIndex].isChecked) selectedIndexes.add(noteIndex)
                else selectedIndexes.remove(noteIndex)
                it.copy(
                    notes = notes,
                    selectedNotesIndexes = selectedIndexes
                )
            }
        }
    }

    private fun deleteNotes() {
        viewModelScope.launch {
            val selectedNotesIds = _state.value.getSelectedNotesIds()
            deleteNotesByIdsUseCase(selectedNotesIds)
        }
    }

    private fun deleteNote(id: Int) {
        viewModelScope.launch {
            deleteNoteByIdUseCase.invoke(id)
        }
    }

    private fun setOperationType(operationType: NotesListUiOperation) {
        _state.update { it.copy(operationType = operationType) }
    }

    private fun setSelectAll(value: Boolean) {
        _state.update { it.copy(selectAllClicked = value) }
    }

    private fun onSelectAllClicked() {
        setSelectAll(_state.value.selectAllClicked.not())
        if (_state.value.selectAllClicked) setAllNotesCheckboxChecked()
        else setAllNotesCheckboxUnchecked()
    }
}