package com.atakanmadanoglu.notesapplication.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class NoteUI(
    val id: Int? = null,
    val title: String,
    val description: String,
    val createdAt: Long
) {
    private var _date: String = run {
        val date = Date(createdAt)
        val formatter = SimpleDateFormat("MMM d, HH:mm", Locale.ENGLISH)
        val current = formatter.format(date)
        current
    }
    val date get() = _date
}