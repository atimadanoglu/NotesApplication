package com.atakanmadanoglu.notesapplication.presentation.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class NoteUI(
    val id: Int = 0,
    val title: String,
    val description: String,
    val createdAt: String,
    var isChecked: MutableState<Boolean> = mutableStateOf(false)
)