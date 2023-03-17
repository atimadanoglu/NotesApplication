package com.atakanmadanoglu.notesapplication.presentation.model

data class NoteUI(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val createdAt: String = "",
    var isChecked: Boolean = false
)