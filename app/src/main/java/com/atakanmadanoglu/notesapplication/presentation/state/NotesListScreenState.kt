package com.atakanmadanoglu.notesapplication.presentation.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.atakanmadanoglu.notesapplication.domain.model.NoteUI

class NotesListScreenState {
    var allNotesList = mutableStateListOf<NoteUI>()
    var listOfSearchedNotes = mutableStateListOf<NoteUI>()
    var totalNotesCount by mutableStateOf(allNotesList.size)
    var searchValue by mutableStateOf("")
}
