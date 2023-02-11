package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atakanmadanoglu.notesapplication.R
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
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .onFocusChanged { focusState ->
                        when {
                            focusState.isFocused -> {
                                editNoteScreenViewModel.updateFocusValue(true)
                            }
                            !focusState.hasFocus -> {
                                editNoteScreenViewModel.updateFocusValue(false)
                            }
                        }
                    },
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
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        when {
                            focusState.isFocused -> {
                                editNoteScreenViewModel.updateFocusValue(true)
                            }
                            !focusState.hasFocus -> {
                                editNoteScreenViewModel.updateFocusValue(false)
                            }
                        }
                    },
                description = editNoteUiState.description,
                onDescriptionChange = { newDescriptionValue ->
                    editNoteScreenViewModel
                        .updateDescriptionValue(newDescriptionValue)
                }
            )
            if (!editNoteUiState.isFocused) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = MaterialTheme.spacing.small)
                        .clickable {
                            editNoteScreenViewModel.deleteNote()
                            navController.popBackStack()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_delete_24),
                        contentDescription = stringResource(id = R.string.delete_icon)
                    )
                    Text(
                        text = stringResource(id = R.string.delete),
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontFamily = MaterialTheme.typography.openSansRegular.fontFamily
                    )
                }
            }
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