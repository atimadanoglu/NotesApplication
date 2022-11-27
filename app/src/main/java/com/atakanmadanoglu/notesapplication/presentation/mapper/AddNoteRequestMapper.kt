package com.atakanmadanoglu.notesapplication.presentation.mapper

import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteRequest
import com.atakanmadanoglu.notesapplication.presentation.state.AddNoteState
import javax.inject.Inject

class AddNoteRequestMapper @Inject constructor() {
    fun mapToAddNoteRequest(
        noteState: AddNoteState
    ) = with(noteState) {
        AddNoteRequest(
            title = title,
            description = description,
            createdAt = noteState.createdAt
        )
    }
}