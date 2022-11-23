package com.atakanmadanoglu.notesapplication.data.model

import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteRequest

object NoteFactory {
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

    fun getMockNoteEntity() = NoteEntity(
        id = 1,
        title = "Hello",
        description = "There",
        createdAt = 1000L
    )
}