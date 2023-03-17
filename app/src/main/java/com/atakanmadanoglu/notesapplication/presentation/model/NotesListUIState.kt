package com.atakanmadanoglu.notesapplication.presentation.model

data class NotesListUIState(
    val allNotesList: List<NoteUI> = emptyList(),
    val searchedNotesList:  List<NoteUI> = emptyList(),
    val totalNotesCount: Int = 0,
    val searchValue: String = "",
    val startChoosingNoteOperation: Boolean = false,
    val openDeleteDialog: Boolean = false,
    val selectAllClicked: Boolean = false,
    val deleteButtonEnabled: Boolean = true,
    val isSearchValueEntered: Boolean = false,
    val selectedNotesCount: Int = 0,
    val selectedNotesIndexes: List<Int> = emptyList()
) {
    fun getSelectedNotesIds(): List<Int> {
        val ids = mutableListOf<Int>()
        allNotesList.map {
            if (it.isChecked) ids.add(it.id)
        }
        return ids
    }
}
