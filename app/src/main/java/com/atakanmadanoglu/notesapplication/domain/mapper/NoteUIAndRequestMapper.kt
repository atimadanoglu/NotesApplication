package com.atakanmadanoglu.notesapplication.domain.mapper

import com.atakanmadanoglu.notesapplication.data.model.Note
import com.atakanmadanoglu.notesapplication.domain.model.NoteUI
import javax.inject.Inject

class NoteUIAndRequestMapper @Inject constructor() {

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
}