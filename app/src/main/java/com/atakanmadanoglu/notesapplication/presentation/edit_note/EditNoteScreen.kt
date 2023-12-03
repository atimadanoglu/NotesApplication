package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.presentation.add_note.NavigationTopAppBar
import com.atakanmadanoglu.notesapplication.presentation.add_note.NoteContentView
import com.atakanmadanoglu.notesapplication.presentation.add_note.TitleInput
import com.atakanmadanoglu.notesapplication.presentation.model.EditNoteUiState
import com.atakanmadanoglu.notesapplication.presentation.notes_list.DeleteAlertDialog
import com.atakanmadanoglu.notesapplication.theme.openSansRegular
import com.atakanmadanoglu.notesapplication.theme.spacing

@Composable
internal fun EditNoteRoute(
    navController: NavController = rememberNavController(),
    editNoteScreenViewModel: EditNoteScreenViewModel = hiltViewModel()
) {
    val editNoteUiState by editNoteScreenViewModel.editNoteUiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val isDoneIconVisible = remember {
        { editNoteUiState.isDoneIconVisible }
    }

    val onTitleChange: (newValue: String) -> Unit = remember {
        return@remember editNoteScreenViewModel::onTitleChanged
    }

    val onDescriptionChange: (newValue: String) -> Unit = remember {
        return@remember editNoteScreenViewModel::onDescriptionChanged
    }

    val popBackStack = remember {
        { navController.popBackStack() }
    }

    val onDeletionApproved: () -> Unit = remember {
        return@remember editNoteScreenViewModel::onDeletionApproved
    }

    val onDoneIconClicked: () -> Unit = remember {
        return@remember editNoteScreenViewModel::onDoneIconClicked
    }

    val clearFocus: () -> Unit = remember {
        { focusManager.clearFocus() }
    }

    EditNoteScreen(
        editNoteUiState = editNoteUiState,
        onNavigationIconClick = { popBackStack() },
        whenIsFocused = editNoteScreenViewModel::whenIsFocused,
        whenNotHaveFocus = editNoteScreenViewModel::whenNotHaveFocus,
        onTitleChange = onTitleChange,
        onDescriptionChange = onDescriptionChange,
        onDeleteClicked = editNoteScreenViewModel::onDeleteButtonClicked,
        onDoneIconClick = {
            onDoneIconClicked()
            clearFocus()
        },
        onDismissRequest = editNoteScreenViewModel::onDeletionDismissed,
        onDeleteOperationApproved = {
            onDeletionApproved()
            popBackStack()
        },
        isDoneIconVisible = isDoneIconVisible
    )

   /* EditNoteScreen(
        editNoteUiState = editNoteUiState,
        onNavigationIconClick = navController::popBackStack,
        whenIsFocused = editNoteScreenViewModel::whenIsFocused,
        whenNotHaveFocus = editNoteScreenViewModel::whenNotHaveFocus,
        onTitleChange = { editNoteScreenViewModel.onTitleChanged(it) },
        onDescriptionChange = { editNoteScreenViewModel.onDescriptionChanged(it) },
        onDeleteClicked = editNoteScreenViewModel::onDeleteButtonClicked,
        onDoneIconClick = {
            editNoteScreenViewModel.onDoneIconClicked()
            focusManager.clearFocus()
        },
        onDismissRequest = editNoteScreenViewModel::onDeletionDismissed,
        onDeleteOperationApproved = {
            editNoteScreenViewModel.onDeletionApproved()
            navController.popBackStack()
        },
        isDoneIconVisible = { editNoteUiState.isDoneIconVisible }
    )*/
}

@Composable
fun EditNoteScreen(
    modifier: Modifier = Modifier,
    editNoteUiState: EditNoteUiState,
    onDoneIconClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
    whenIsFocused: () -> Unit,
    whenNotHaveFocus: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDeleteClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    onDeleteOperationApproved: () -> Unit,
    isDoneIconVisible: () -> Boolean
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.small),
        topBar = {
            NavigationTopAppBar(
                isDoneIconVisible = isDoneIconVisible,
                onDoneIconClick = onDoneIconClick,
                onNavigationIconClick = onNavigationIconClick
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
                            focusState.isFocused -> whenIsFocused()
                            !focusState.hasFocus -> whenNotHaveFocus()
                        }
                    },
                title = editNoteUiState.title,
                onTitleChange = { title -> onTitleChange(title) },
            )
            ShowDate(date = editNoteUiState.createdAt)
            NoteContentView(
                description = editNoteUiState.description,
                onDescriptionChange = { desc -> onDescriptionChange(desc) },
            )
            if (!editNoteUiState.isFocused) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = MaterialTheme.spacing.small)
                        .clickable {
                            onDeleteClicked()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    DeleteButton()
                }
            }
        }
        if (editNoteUiState.openDeleteDialog) {
            DeleteAlertDialog(
                selectedNoteCount = 1, // default value is 1
                onDismissRequest = onDismissRequest,
                onDeleteOperationApproved = onDeleteOperationApproved
            )
        }
    }
}

@Composable
private fun DeleteButton() {
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
    /*EditNoteScreen(navController = rememberNavController())*/
}