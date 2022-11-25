package com.atakanmadanoglu.notesapplication.presentation.add_note.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.atakanmadanoglu.notesapplication.presentation.add_note.AddNoteScreen
import com.atakanmadanoglu.notesapplication.presentation.navigation.Screen

fun NavController.navigateToAddNote() {
    this.navigate(Screen.AddNoteScreen.route)
}

fun NavGraphBuilder.addNoteScreen(
    doneIconOnClick: () -> Unit
) {
    composable(route = Screen.AddNoteScreen.route) {
        AddNoteScreen(
            doneIconOnClick = doneIconOnClick
        )
    }
}