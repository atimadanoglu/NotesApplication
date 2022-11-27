package com.atakanmadanoglu.notesapplication.presentation.add_note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.ui.theme.spacing
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    addNoteViewModel: AddNoteViewModel = hiltViewModel()
) {
    val addNoteState by remember {
        mutableStateOf(addNoteViewModel.addNoteState.value)
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.small),
        topBar = {
            NavigationTopAppBar(
                isDoneIconEnabled = addNoteState.isDoneIconEnabled(),
                doneIconOnClick = {
                    addNoteViewModel.addNote(
                        inputTitle = addNoteState.title,
                        inputDescription = addNoteState.description
                    )
                    navController.popBackStack() },
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
                title = addNoteState.title,
                titleOnChange = { newTitleValue ->
                    addNoteState.title = newTitleValue
                }
            )
            ShowDate()
            NoteContentView(
                description = addNoteState.description,
                onDescriptionChange = { newDescriptionValue ->
                    addNoteState.description = newDescriptionValue
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationTopAppBar(
    isDoneIconEnabled: Boolean,
    doneIconOnClick: () -> Unit,
    navigationIconOnClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            IconButton(
                onClick = navigationIconOnClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = stringResource(id = R.string.back_button)
                )
            }
        },
        actions = {
            IconButton(
                onClick = doneIconOnClick,
                enabled = isDoneIconEnabled
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_done_24),
                    contentDescription = stringResource(id = R.string.done_button)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleInput(
    modifier: Modifier = Modifier,
    title: String,
    titleOnChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent),
        value = title,
        onValueChange = titleOnChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.title),
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
        },
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.SemiBold
        ),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteContentView(
    modifier: Modifier = Modifier,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        value = description,
        onValueChange = onDescriptionChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.content),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        },
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        ),
        singleLine = false,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun ShowDate() {
    val calendar = Calendar.getInstance().time
    val formatter = SimpleDateFormat("MMM d, HH:mm", Locale.ENGLISH)
    val current = formatter.format(calendar)
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = current,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        color = Color.Gray,
        textAlign = TextAlign.End
    )
}

@Preview
@Composable
private fun PreviewAddNoteScreenComponents() {
    //NavigationTopAppBar(doneIconOnClick = {}, navigationIconOnClick = {})
}