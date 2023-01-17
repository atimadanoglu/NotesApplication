package com.atakanmadanoglu.notesapplication.presentation.edit_note.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.atakanmadanoglu.notesapplication.presentation.edit_note.EditNoteScreen
import com.atakanmadanoglu.notesapplication.presentation.navigation.Screen

private const val noteIdArg = "noteId"

internal class EditNoteArgs(val noteId: Int) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(checkNotNull(savedStateHandle[noteIdArg]) as Int)
}

fun NavController.navigateToEditNoteScreen(
    noteId: Int
) {
    this.navigate(
        route = Screen.EditNoteScreen.route
                + "/$noteId"
    )
}

fun NavGraphBuilder.editNoteScreen(
    navController: NavController
) {
    composable(
        route = Screen.EditNoteScreen.route +
                "/{$noteIdArg}",
        arguments = listOf(
            navArgument(noteIdArg) {
                type = NavType.IntType
            }
        )
    ) {
        EditNoteScreen(navController = navController)
    }
}