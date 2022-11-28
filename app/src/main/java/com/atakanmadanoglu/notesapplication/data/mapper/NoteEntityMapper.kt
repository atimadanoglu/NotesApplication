package com.atakanmadanoglu.notesapplication.data.mapper

import com.atakanmadanoglu.notesapplication.data.model.NoteEntity
import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
import java.util.*
import javax.inject.Inject

class NoteEntityMapper @Inject constructor() {

    fun mapToNoteDomain(
        noteEntity: NoteEntity
    ) = with(noteEntity) {
        NoteDomain(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt
        )
    }

    fun mapToNoteEntity(
        noteDomain: NoteDomain
    )  = with(noteDomain) {
        NoteEntity(
            id = id,
            title = title,
            description = description,
            createdAt = Date().time
        )
    }
}