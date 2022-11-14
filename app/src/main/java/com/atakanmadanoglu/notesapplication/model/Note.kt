package com.atakanmadanoglu.notesapplication.model

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long
)