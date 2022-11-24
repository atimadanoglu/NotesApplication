package com.atakanmadanoglu.notesapplication.data.model

data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: Long
)