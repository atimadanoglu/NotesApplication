package com.atakanmadanoglu.notesapplication.data.mapper

import com.atakanmadanoglu.notesapplication.data.model.NoteEntity
import com.atakanmadanoglu.notesapplication.model.Note
import javax.inject.Inject

class NoteEntityMapper @Inject constructor() {

    fun mapToEntity(
        note: Note
    ) = with(note) {
        NoteEntity(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt
        )
    }

    fun mapToNote(
        noteEntity: NoteEntity
    ) = with(noteEntity) {
        Note(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt
        )
    }
}