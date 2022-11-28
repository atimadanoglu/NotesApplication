package com.atakanmadanoglu.notesapplication.domain.model

import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI

object UseCaseVariousNotesFactory {
    fun getMockNoteUI() = NoteUI(
        id = 1,
        title = "Hello",
        description = "There",
        createdAt = 1000L
    )

    fun getMockNoteDomain() = NoteDomain(
        id = 1,
        title = "Hello",
        description = "There",
        createdAt = 1000L
    )
}