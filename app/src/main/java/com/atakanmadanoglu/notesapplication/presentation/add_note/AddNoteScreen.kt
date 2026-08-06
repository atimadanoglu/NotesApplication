package com.atakanmadanoglu.notesapplication.presentation.add_note

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteAction
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteUIState
import com.atakanmadanoglu.notesapplication.theme.openSansRegular
import com.atakanmadanoglu.notesapplication.theme.openSansSemiBold
import com.atakanmadanoglu.notesapplication.theme.spacing

@Composable
fun AddNoteRoute(
    navController: NavController,
    viewModel: AddNoteViewModel = hiltViewModel()
) {
    val addNoteState by viewModel.state.collectAsStateWithLifecycle()

    AddNoteScreen(
        addNoteState = addNoteState,
        onAction = { action ->
            when (action) {
                AddNoteAction.OnBackClicked -> navController.navigateUp()
                AddNoteAction.OnDoneClicked -> {
                    viewModel.addNote()
                    navController.navigateUp()
                }
                is AddNoteAction.OnDescriptionChanged -> viewModel.setDescription(action.desc)
                is AddNoteAction.OnTitleChanged -> viewModel.setTitle(action.title)
            }
        }
    )
}

@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier,
    addNoteState: AddNoteUIState,
    onAction: (AddNoteAction) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.small),
        topBar = {
            NavigationTopAppBar(
                isDoneIconVisible = addNoteState.isAnyValueChanged,
                onDoneIconClick = { onAction(AddNoteAction.OnDoneClicked) },
                onNavigationIconClick = { onAction(AddNoteAction.OnBackClicked) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TitleInput(
                title = addNoteState.title,
                onTitleChange = { title -> onAction(AddNoteAction.OnTitleChanged(title)) }
            )
            ShowDate(currentTime = addNoteState.currentTime)
            NoteContentView(
                description = addNoteState.description,
                onDescriptionChange = { desc -> onAction(AddNoteAction.OnDescriptionChanged(desc)) }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopAppBar(
    isDoneIconVisible: Boolean,
    onDoneIconClick: () -> Unit,
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            SpecificIconButton(
                onClick = onNavigationIconClick,
                id = R.drawable.ic_baseline_arrow_back_24,
                contentDesc = stringResource(id = R.string.back_button)
            )
        },
        actions = {
            if (isDoneIconVisible) {
                SpecificIconButton(
                    onClick = onDoneIconClick,
                    id = R.drawable.ic_baseline_done_24,
                    contentDesc = stringResource(id = R.string.done_button)
                )
            }
        }
    )
}

@Composable
private fun SpecificIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes id: Int,
    contentDesc: String
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = id),
            contentDescription = contentDesc
        )
    }
}

@Composable
fun TitleInput(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit
) {
    Column( modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        OutlinedTextField(

            value = title,
            onValueChange = onTitleChange,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.title),
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontFamily = MaterialTheme.typography.openSansSemiBold.fontFamily
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontFamily = MaterialTheme.typography.openSansSemiBold.fontFamily
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
        )
    }
}

@Composable
fun NoteContentView(
    modifier: Modifier = Modifier,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxSize(),
        value = description,
        onValueChange = onDescriptionChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.content),
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontFamily = MaterialTheme.typography.openSansRegular.fontFamily
            )
        },
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontFamily = MaterialTheme.typography.openSansRegular.fontFamily
        ),
        singleLine = false,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun ShowDate(currentTime: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = currentTime,
        fontFamily = MaterialTheme.typography.openSansRegular.fontFamily,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        color = Color.Gray,
        textAlign = TextAlign.End
    )
}

@Preview
@Composable
private fun AddNotePrev() {
    AddNoteScreen(
        addNoteState = AddNoteUIState()
    ) { }
}