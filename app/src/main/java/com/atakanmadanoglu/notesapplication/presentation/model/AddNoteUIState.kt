package com.atakanmadanoglu.notesapplication.presentation.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class AddNoteUIState(
    val title: String = "",
    val description: String = "",
    val createdAt: String = ""
){
    val isAnyValueChanged: Boolean
        get() = title.isNotEmpty() || description.isNotEmpty()

    val currentTime: String
        get() {
            val calendar = Calendar.getInstance().time
            val formatter = SimpleDateFormat("MMM d, HH:mm", Locale.ENGLISH)
            val current = formatter.format(calendar)
            return current
        }
}

sealed interface AddNoteAction {
    object OnBackClicked: AddNoteAction
    object OnDoneClicked: AddNoteAction
    data class OnDescriptionChanged(val desc: String): AddNoteAction
    data class OnTitleChanged(val title: String): AddNoteAction
}