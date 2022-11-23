package com.atakanmadanoglu.notesapplication.presentation.model

data class AddNoteRequest(
    val title: String,
    val description: String,
    val createdAt: Long
)