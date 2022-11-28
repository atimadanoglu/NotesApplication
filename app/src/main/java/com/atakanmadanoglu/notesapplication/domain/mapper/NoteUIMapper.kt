package com.atakanmadanoglu.notesapplication.domain.mapper

import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import javax.inject.Inject

class NoteUIMapper @Inject constructor() {

    fun mapToNoteUI(
        noteDomain: NoteDomain
    ) = with(noteDomain) {
        NoteUI(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt
        )
    }
}