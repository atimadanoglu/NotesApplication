package com.atakanmadanoglu.notesapplication.presentation.navigation

sealed class Screen(val route: String) {
    object NotesListScreen: Screen("notes_list")
    object AddNoteScreen: Screen("add_note_screen")
}
