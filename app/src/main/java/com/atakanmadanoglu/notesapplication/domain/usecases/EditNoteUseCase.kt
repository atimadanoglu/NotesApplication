package com.atakanmadanoglu.notesapplication.domain.usecases

import com.atakanmadanoglu.notesapplication.data.repository.notes.NotesRepository
import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
import javax.inject.Inject

class EditNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(
        id: Int,
        title: String,
        description: String
    ) = notesRepository.editNote(NoteDomain(
        id = id,
        title = title,
        description = description
    ))
}