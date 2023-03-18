package com.atakanmadanoglu.notesapplication.domain.model

import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import java.text.SimpleDateFormat
import java.util.*

object UseCaseVariousNotesFactory {
    fun getMockNoteUI(createdAt: Long): NoteUI {
        val date = Date(createdAt)
        val formatter = SimpleDateFormat("MMM d, HH:mm", Locale.ENGLISH)
        val current = formatter.format(date)
        return NoteUI(
            id = 1,
            title = "Hello",
            description = "There",
            createdAt = current
        )
    }

    fun getMockNoteDomain() = NoteDomain(
        id = 1,
        title = "Hello",
        description = "There",
        createdAt = Date().time
    )
}