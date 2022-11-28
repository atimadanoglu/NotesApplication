package com.atakanmadanoglu.notesapplication.domain.mapper

import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteUIMapper @Inject constructor() {

    fun mapToNoteUI(
        noteDomain: NoteDomain
    ) = with(noteDomain) {
        val date = Date(noteDomain.createdAt)
        val formatter = SimpleDateFormat("MMM d, HH:mm", Locale.ENGLISH)
        val current = formatter.format(date)
        return@with NoteUI(
            id = id,
            title = title,
            description = description,
            createdAt = current
        )
    }
}