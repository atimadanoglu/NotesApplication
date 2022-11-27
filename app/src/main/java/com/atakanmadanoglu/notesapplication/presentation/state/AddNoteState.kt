package com.atakanmadanoglu.notesapplication.presentation.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AddNoteState {
    var createdAt by mutableStateOf(0L)
    var title by mutableStateOf("")
    var description by mutableStateOf("")

    fun isDoneIconEnabled(): Boolean  {
        return title.isNotEmpty() && description.isNotEmpty()
    }
}