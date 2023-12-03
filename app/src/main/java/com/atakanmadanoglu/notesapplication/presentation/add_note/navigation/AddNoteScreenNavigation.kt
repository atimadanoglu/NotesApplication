package com.atakanmadanoglu.notesapplication.presentation.add_note.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.atakanmadanoglu.notesapplication.presentation.add_note.AddNoteRoute
import com.atakanmadanoglu.notesapplication.presentation.navigation.Screen

fun NavController.navigateToAddNote() {
    this.navigate(Screen.ADD_NOTE.route)
}

fun NavGraphBuilder.addNoteScreen(
    navController: NavController
) {
    composable(route = Screen.ADD_NOTE.route) {
        AddNoteRoute(navController = navController)
    }
}