package com.atakanmadanoglu.notesapplication.presentation.model

/*data class DisplayNotesState(
    val totalNotesCount: Int = 0
)

data class SelectNotesState(
    val selectedNotesCount: Int = 0,
    val selectedNotesIndexes: List<Int> = emptyList()
)*/

data class NotesListUIState(
    val operationType: NotesListUiOperation =
        NotesListUiOperation.DisplayNotes,
    val allNotesList: List<NoteUI> = emptyList(),
    val searchedNotesList:  List<NoteUI> = emptyList(),
    val totalNotesCount: Int = 0,
    val searchValue: String = "",
    val showDeletionDialog: Boolean = false,
    val selectAllClicked: Boolean = false,
    val isSearchValueEntered: Boolean = false,
    val selectedNotesCount: Int = 0,
    val selectedNotesIndexes: List<Int> = emptyList()
) {
    fun getSelectedNotesIds(): List<Int> {
        return allNotesList
            .filter { it.isChecked }
            .map { it.id }
    }

    fun isDeleteButtonEnabled(): Boolean = selectedNotesCount != 0
    fun isAllSelected(): Boolean = (selectedNotesCount == totalNotesCount)
}

enum class NotesListUiOperation {
    DisplayNotes, SelectNotes
}

sealed interface NotesListUiEvent {
    data class NoteCardLongPressed(val noteIndex: Int): NotesListUiEvent
    data class CheckboxClicked(val noteIndex: Int): NotesListUiEvent
    data class SearchValueChanged(val newInput: String): NotesListUiEvent
    object DeleteButtonClicked: NotesListUiEvent
    object DeletionApproved: NotesListUiEvent
    object DeletionDismissed: NotesListUiEvent
    object SelectAllButtonClicked: NotesListUiEvent
    object CancelButtonClicked: NotesListUiEvent
}