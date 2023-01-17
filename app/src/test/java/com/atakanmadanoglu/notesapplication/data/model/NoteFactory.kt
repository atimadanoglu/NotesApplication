package com.atakanmadanoglu.notesapplication.data.model

import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain

object NoteFactory {
    fun getMockNoteDomain() = NoteDomain(
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