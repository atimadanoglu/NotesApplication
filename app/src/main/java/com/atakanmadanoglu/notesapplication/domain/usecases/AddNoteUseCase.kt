package com.atakanmadanoglu.notesapplication.domain.usecases

import com.atakanmadanoglu.notesapplication.data.repository.notes.NotesRepository
import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(
        inputTitle: String,
        inputDescription: String
    ) = notesRepository.addNote(NoteDomain(
        title = inputTitle,
        description = inputDescription
    ))
}