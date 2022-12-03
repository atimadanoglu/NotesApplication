package com.atakanmadanoglu.notesapplication.presentation.notes_list.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.atakanmadanoglu.notesapplication.presentation.navigation.Screen
import com.atakanmadanoglu.notesapplication.presentation.notes_list.NotesListScreen

fun NavController.navigateToNotesListScreen() {
    this.navigate(Screen.NotesListScreen.route)
}

fun NavGraphBuilder.notesList(
    navigateToAddNoteScreen: () -> Unit,
    navigateToEditNoteScreen: (Int) -> Unit
) {
    composable(route = Screen.NotesListScreen.route) {
        NotesListScreen(
            addNoteButtonClicked = navigateToAddNoteScreen,
            cardOnClick = navigateToEditNoteScreen
        )
    }
}