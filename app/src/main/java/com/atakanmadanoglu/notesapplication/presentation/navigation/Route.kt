package com.atakanmadanoglu.notesapplication.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object NoteList: Route
    @Serializable
    data object AddNote: Route
    @Serializable
    data class EditNote(val id: Int): Route
}