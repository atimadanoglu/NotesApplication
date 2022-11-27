package com.atakanmadanoglu.notesapplication.presentation.mapper

import com.atakanmadanoglu.notesapplication.presentation.add_note.AddNoteUiState
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteRequest
import javax.inject.Inject

class AddNoteRequestMapper @Inject constructor() {
    fun mapToAddNoteRequest(
        noteState: AddNoteUiState
    ) = with(noteState) {
        AddNoteRequest(
            title = title,
            description = description,
            createdAt = noteState.createdAt
        )
    }
}