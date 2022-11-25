package com.atakanmadanoglu.notesapplication.domain.model

import com.atakanmadanoglu.notesapplication.data.model.Note
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteRequest

object UseCaseVariousNotesFactory {
    fun getMockNoteUI() = NoteUI(
        id = 1,
        title = "Hello",
        description = "There",
        createdAt = 1000L
    )

    fun getMockNote() = Note(
        id = 1,
        title = "Hello",
        description = "There",
        createdAt = 1000L
    )

    fun getMockAddNoteRequest() = AddNoteRequest(
        title = "Hello",
        description = "There",
        createdAt = 1000L
    )
}