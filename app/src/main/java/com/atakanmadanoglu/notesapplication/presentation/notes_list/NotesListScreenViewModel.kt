package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.usecases.DeleteNotesByIdsUseCase
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
    val startChoosingNoteOperation: Boolean
    val openDeleteDialog: Boolean
    fun isSearchValueEntered(): Boolean
}

private class MutableNotesListUiState: NotesListUiState {
    override var allNotesList = mutableStateListOf<NoteUI>()
    override var searchedNotesList = mutableStateListOf<NoteUI>()
    override var totalNotesCount by mutableStateOf(allNotesList.size)
    override var searchValue by mutableStateOf("")
    override var startChoosingNoteOperation by mutableStateOf(false)
    override var openDeleteDialog by mutableStateOf(false)

    override fun isSearchValueEntered(): Boolean {
        return searchValue.isNotEmpty()
    }
}

@HiltViewModel
class NotesListScreenViewModel @Inject constructor(
    private val getNotesByCreatedAtUseCase: GetNotesByCreatedAtUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val deleteNotesByIdsUseCase: DeleteNotesByIdsUseCase
): ViewModel() {
    private val _notesListUiState = MutableNotesListUiState()
    val notesListUiState: NotesListUiState = _notesListUiState

    fun getAllNotes() {
        viewModelScope.launch {
            getNotesByCreatedAtUseCase.invoke().collectLatest { list ->
                with(_notesListUiState) {
                    allNotesList.clear()
                    list.map { noteUI ->
                        noteUI?.let {
                            allNotesList.add(it)
                        }
                    }
                    totalNotesCount = list.size
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

    fun setStartChoosingNoteOperation(
        value: Boolean,
        noteIndex: Int
    ) {
        _notesListUiState.startChoosingNoteOperation = value
        if (value) {
            setNoteCheckedState(noteIndex)
        }
    }

    fun setAllNotesCheckboxUnchecked() {
        _notesListUiState.allNotesList.map {
            it.isChecked.value = false
        }
    }

    fun setAllNotesCheckboxChecked() {
        _notesListUiState.allNotesList.map {
            it.isChecked.value = true
        }
    }

    fun setOpenDeleteDialog(value: Boolean) {
        _notesListUiState.openDeleteDialog = value
    }

    fun getSelectedNotesCount(): Int {
        return _notesListUiState
            .allNotesList
            .count {
                it.isChecked.value
            }
    }

    fun isDeleteButtonEnabled(): Boolean {
        val selectedNotesCount = getSelectedNotesCount()
        return selectedNotesCount != 0
    }

    private fun getSelectedNotesIds(): List<Int> {
        val checkedNotesIds = mutableListOf<Int>()
        _notesListUiState.allNotesList.map {
            if (it.isChecked.value) {
                checkedNotesIds.add(it.id)
            }
        }
        return checkedNotesIds
    }

    fun setNoteCheckedState(noteIndex: Int) {
        with(_notesListUiState.allNotesList[noteIndex]) {
            isChecked.value = !isChecked.value
        }
    }

    private fun deleteNotes() {
        val selectedNotesIds = getSelectedNotesIds()
        viewModelScope.launch {
            deleteNotesByIdsUseCase.invoke(selectedNotesIds)
        }
    }

    fun cancelChoosingNoteOperation() {
        setStartChoosingNoteOperation(false, -1)
        setAllNotesCheckboxUnchecked()
    }

    fun onDeleteOperationApproved() {
        deleteNotes()
        setOpenDeleteDialog(false)
        setStartChoosingNoteOperation(false, -1)
    }
}