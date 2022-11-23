package com.atakanmadanoglu.notesapplication.domain

import com.atakanmadanoglu.notesapplication.data.repository.NotesRepository
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteRequest
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(
        addNoteRequest: AddNoteRequest
    ) = notesRepository.addNote(addNoteRequest)
}