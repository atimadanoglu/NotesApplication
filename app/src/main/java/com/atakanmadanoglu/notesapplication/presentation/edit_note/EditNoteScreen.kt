package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atakanmadanoglu.notesapplication.presentation.add_note.NavigationTopAppBar
import com.atakanmadanoglu.notesapplication.presentation.add_note.NoteContentView
import com.atakanmadanoglu.notesapplication.presentation.add_note.TitleInput
import com.atakanmadanoglu.notesapplication.theme.openSansRegular
import com.atakanmadanoglu.notesapplication.theme.spacing
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    editNoteScreenViewModel: EditNoteScreenViewModel = hiltViewModel()
) {
    val editNoteUiState by remember {
        mutableStateOf(editNoteScreenViewModel.editNoteUiState)
    }
    // To get note by id that is sent from previous page
    editNoteScreenViewModel.getNoteById()

    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.small),
        topBar = {
            NavigationTopAppBar(
                isDoneIconVisible = editNoteUiState.isDoneIconVisible,
                doneIconOnClick = {
                    editNoteScreenViewModel.editNote(
                        inputTitle = editNoteUiState.title,
                        inputDescription = editNoteUiState.description
                    )
                    focusManager.clearFocus() },
                navigationIconOnClick = { navController.popBackStack() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TitleInput(
                title = editNoteUiState.title,
                titleOnChange = { newTitleValue ->
                    editNoteScreenViewModel
                        .updateTitleValue(newTitleValue)
                }
            )
            ShowDate(
                date = editNoteUiState.createdAt
            )
            NoteContentView(
                description = editNoteUiState.description,
                onDescriptionChange = { newDescriptionValue ->
                    editNoteScreenViewModel
                        .updateDescriptionValue(newDescriptionValue)
                }
            )
        }
    }
}

@Composable
private fun ShowDate(
    date: String
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = date,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontFamily = MaterialTheme.typography.openSansRegular.fontFamily,
        color = Color.Gray,
        textAlign = TextAlign.End
    )
}

@Preview
@Composable
fun Preview() {
    EditNoteScreen(navController = rememberNavController())
}