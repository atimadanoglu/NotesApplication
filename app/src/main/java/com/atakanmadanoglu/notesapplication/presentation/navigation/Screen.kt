package com.atakanmadanoglu.notesapplication.presentation.navigation

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object AddNoteScreen: Screen("add_note_screen")
}
