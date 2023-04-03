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
) {
    fun isNewValueEntered(
        retrievedTitle: String,
        retrievedDescription: String
    ): Boolean {
        return !title.contentEquals(retrievedTitle) ||
                !description.contentEquals(retrievedDescription)
    }
}

sealed interface EditNoteScreenEvent {
    data class TitleChanged(val newInput: String): EditNoteScreenEvent
    data class DescriptionChanged(val newInput: String): EditNoteScreenEvent
    object WhenIsFocused: EditNoteScreenEvent
    object WhenNotHaveFocus: EditNoteScreenEvent
    object DeleteButtonClicked: EditNoteScreenEvent
    object DoneIconClicked: EditNoteScreenEvent
    object DeletionDismissed: EditNoteScreenEvent
    object DeletionApproved: EditNoteScreenEvent
}