package com.atakanmadanoglu.notesapplication.data.mapper

import com.atakanmadanoglu.notesapplication.data.model.Note
import com.atakanmadanoglu.notesapplication.data.model.NoteEntity
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteRequest
import javax.inject.Inject

class NoteEntityMapper @Inject constructor() {

    fun mapToEntity(
        noteRequest: AddNoteRequest
    ) = with(noteRequest) {
        NoteEntity(
            title = title,
            description = description,
            createdAt = createdAt
        )
    }

    fun mapToNote(
        noteEntity: NoteEntity
    ) = with(noteEntity) {
        Note(
            id = id ?: -1,
            title = title,
            description = description,
            createdAt = createdAt
        )
    }
}