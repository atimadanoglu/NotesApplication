package com.atakanmadanoglu.notesapplication.presentation.model

data class EditNoteUiState(
    val title: String = "",
    val description: String = "",
    val createdAt: String = "",
    val isDoneIconClicked: Boolean = false,
    val isDoneIconVisible: Boolean = false,
    val isFocused: Boolean = false,
    val isDeleteButtonClicked: Boolean = false,
    val openDeleteDialog: Boolean = false
)

sealed interface EditNoteAction {
    data object OnDoneIconClicked: EditNoteAction
    data object OnBackClicked: EditNoteAction
    data class OnTitleChanged(val title: String): EditNoteAction
    data class OnDescriptionChanged(val desc: String): EditNoteAction
    data object OnDeleteClicked: EditNoteAction
    data object OnDismissRequest: EditNoteAction
    data object OnDeleteApproved: EditNoteAction
    data class OnFocusChanged(val isFocused: Boolean): EditNoteAction
}