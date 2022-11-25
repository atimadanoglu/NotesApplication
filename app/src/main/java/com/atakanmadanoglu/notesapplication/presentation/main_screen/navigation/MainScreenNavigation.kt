package com.atakanmadanoglu.notesapplication.presentation.main_screen.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.atakanmadanoglu.notesapplication.presentation.main_screen.MainScreen
import com.atakanmadanoglu.notesapplication.presentation.navigation.Screen


fun NavController.navigateToMainScreen() {
    this.navigate(Screen.MainScreen.route)
}

fun NavGraphBuilder.mainScreen(
    navigateToAddNoteScreen: () -> Unit
) {
    composable(route = Screen.MainScreen.route) {
        MainScreen(
            navigateToAddNoteScreen = navigateToAddNoteScreen
        )
    }
}