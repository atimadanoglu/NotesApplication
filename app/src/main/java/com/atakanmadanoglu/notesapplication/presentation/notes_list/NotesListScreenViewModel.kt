package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.di.DefaultDispatcher
import com.atakanmadanoglu.notesapplication.domain.GetNotesUseCase
import com.atakanmadanoglu.notesapplication.domain.model.NoteUI
import com.atakanmadanoglu.notesapplication.presentation.state.NotesListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListScreenViewModel @Inject constructor(
    getNotesUseCase: GetNotesUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
): ViewModel() {
    private val _notesListScreenState = MutableStateFlow(NotesListScreenState())
    val notesListState = _notesListScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            getNotesUseCase.invoke().collectLatest {
                with(_notesListScreenState.value) {
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
        with(_notesListScreenState.value) {
            listOfSearchedNotes = allNotesList
            listOfSearchedNotes.filter { noteUI ->
                noteUI.title.contains(searchText, ignoreCase = true) ||
                        noteUI.description.contains(searchText, ignoreCase = true)
            }
            searchValue = searchText
        }
    }

    private val isSearchValueEmpty = MutableStateFlow(_notesListScreenState.value.searchValue.isEmpty())

    fun decideWhichListWillBeUsed(): List<NoteUI> = with(_notesListScreenState.value) {
        return if (isSearchValueEmpty.value) {
            allNotesList
        } else {
            listOfSearchedNotes
        }
    }
}