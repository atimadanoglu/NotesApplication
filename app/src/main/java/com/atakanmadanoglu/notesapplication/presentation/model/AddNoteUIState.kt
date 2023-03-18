package com.atakanmadanoglu.notesapplication.presentation.model

data class AddNoteUIState(
    val title: String = "",
    val description: String = "",
    val createdAt: String = ""
){
    fun isBothTitleAndDescriptionEntered(): Boolean {
        return title.isNotEmpty() || description.isNotEmpty()
    }
}