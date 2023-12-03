package com.atakanmadanoglu.notesapplication.presentation.notes_list.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.atakanmadanoglu.notesapplication.presentation.navigation.Screen
import com.atakanmadanoglu.notesapplication.presentation.notes_list.NoteListRoute

fun NavGraphBuilder.noteListScreen(
    navigateToAddNoteScreen: () -> Unit,
    navigateToEditNoteScreen: (Int) -> Unit
) {
    composable(route = Screen.NOTE_LIST.route) {
        NoteListRoute(
            addNoteButtonClicked = navigateToAddNoteScreen,
            cardOnClick = navigateToEditNoteScreen
        )
    }
}