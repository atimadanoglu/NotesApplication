package com.atakanmadanoglu.notesapplication.presentation.model

data class NotesListUIState(
    val operationType: NotesListUiOperation =
        NotesListUiOperation.DisplayNotes,
    val notes: List<NoteUI> = emptyList(),
    val searchedNotesList:  List<NoteUI> = emptyList(),
    val totalNotesCount: Int = 0,
    val searchText: String = "",
    val showDeletionDialog: Boolean = false,
    val selectAllClicked: Boolean = false,
    val isSearchValueEntered: Boolean = false,
    val selectedNotesCount: Int = 0,
    val selectedNotesIndexes: List<Int> = emptyList()
) {
    fun getSelectedNotesIds(): List<Int> {
        return notes
            .filter { it.isChecked }
            .map { it.id }
    }

    fun isAllSelected(): Boolean = (selectedNotesCount == totalNotesCount)
}

enum class NotesListUiOperation { DisplayNotes, SelectNotes }