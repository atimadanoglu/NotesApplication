package com.atakanmadanoglu.notesapplication.domain.mapper

import com.atakanmadanoglu.notesapplication.domain.model.NoteUI
import com.atakanmadanoglu.notesapplication.model.Note
import javax.inject.Inject

class NoteUIMapper @Inject constructor() {

    fun mapToNoteUI(
        note: Note
    ) = with(note) {
        NoteUI(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt
        )
    }

    fun mapToNote(
        noteUI: NoteUI
    ) = with(noteUI) {
        Note(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt
        )
    }
}