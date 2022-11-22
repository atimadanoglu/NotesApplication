package com.atakanmadanoglu.notesapplication.data.model

import com.atakanmadanoglu.notesapplication.model.Note

object NoteFactory {
    fun getMockNote() = Note(
        id = 1,
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