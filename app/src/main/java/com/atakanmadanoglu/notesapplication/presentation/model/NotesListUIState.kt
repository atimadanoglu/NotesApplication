package com.atakanmadanoglu.notesapplication.presentation.model

data class NotesListUIState(
    val operationType: NotesListUiOperation =
        NotesListUiOperation.DisplayNotes,
    val notes: List<NoteUI> = emptyList(),
    val displayedNotes: List<NoteUI> = emptyList(),
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
}

enum class NotesListUiOperation { DisplayNotes, SelectNotes }

sealed interface NoteListEvent {
    data object AddNoteButtonClicked : NoteListEvent
    data object MakeSearchValueEmpty : NoteListEvent
    data class OnCardClick(val noteId: Int, val noteIndex: Int) : NoteListEvent
    data class OnCardLongClick(val noteIndex: Int) : NoteListEvent
    data class OnSearchValueChange(val newValue: String) : NoteListEvent
    data object CancelChoosingNote : NoteListEvent
    data object OnDeleteClicked : NoteListEvent
    data object OnSelectAllClicked : NoteListEvent
    data object OnDismissRequest : NoteListEvent
    data object OnDeleteOperationApproved : NoteListEvent
    data class UpdateCheckboxState(val index: Int) : NoteListEvent
    data class OnSwiped(val index: Int) : NoteListEvent
}