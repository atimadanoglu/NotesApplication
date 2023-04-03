package com.atakanmadanoglu.notesapplication.presentation.model

data class AddNoteUIState(
    val title: String = "",
    val description: String = "",
    val createdAt: String = ""
){
    fun isAnyValueEntered(): Boolean {
        return title.isNotEmpty() || description.isNotEmpty()
    }
}

sealed interface AddNoteUiEvent {
    data class TitleChanged(val newInput: String): AddNoteUiEvent
    data class DescriptionChanged(val newInput: String): AddNoteUiEvent
    object DoneIconClicked: AddNoteUiEvent
}