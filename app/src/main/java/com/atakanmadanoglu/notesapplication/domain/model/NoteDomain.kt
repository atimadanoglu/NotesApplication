package com.atakanmadanoglu.notesapplication.domain.model

data class NoteDomain(
    val id: Int = 0,
    val title: String,
    val description: String,
    val createdAt: Long = 0
)