package com.atakanmadanoglu.notesapplication.presentation.add_note

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteUIState
import com.atakanmadanoglu.notesapplication.theme.openSansRegular
import com.atakanmadanoglu.notesapplication.theme.openSansSemiBold
import com.atakanmadanoglu.notesapplication.theme.spacing
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddNoteRoute(
    navController: NavController,
    addNoteViewModel: AddNoteViewModel = hiltViewModel()
) {
    val addNoteState by addNoteViewModel.state.collectAsStateWithLifecycle()

    val onTitleChanged: (newValue: String) -> Unit = remember {
        return@remember addNoteViewModel::onTitleChanged
    }
    val onDescriptionChanged: (newValue: String) -> Unit = remember {
        return@remember addNoteViewModel::onDescriptionChanged
    }
    val onDoneIconClicked: () -> Unit = remember {
        return@remember addNoteViewModel::onDoneIconClicked
    }
    val popBackStack: () -> Unit = remember {
        { navController.popBackStack() }
    }
    val isDoneIconVisible: () -> Boolean by remember {
        derivedStateOf {
            { addNoteState.isAnyValueEntered() }
        }
    }

    AddNoteScreen(
        addNoteState = addNoteState,
        onNavigationIconClick = navController::popBackStack,
        onTitleChange = onTitleChanged,
        onDescriptionChange = onDescriptionChanged,
        onDoneIconClick = {
            onDoneIconClicked()
            popBackStack()
        },
        isDoneIconVisible = isDoneIconVisible
    )
}

@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier,
    addNoteState: AddNoteUIState,
    onNavigationIconClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDoneIconClick: () -> Unit,
    isDoneIconVisible: () -> Boolean
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.small),
        topBar = {
            NavigationTopAppBar(
                isDoneIconVisible = isDoneIconVisible/*{ addNoteState.isAnyValueEntered() }*/,
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
                title = addNoteState.title,
                onTitleChange = onTitleChange
            )
            ShowDate()
            NoteContentView(
                description = addNoteState.description,
                onDescriptionChange = onDescriptionChange
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopAppBar(
    isDoneIconVisible: () -> Boolean,
    onDoneIconClick: () -> Unit,
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        title = {  },
        navigationIcon = {
            SpecificIconButton(
                onClick = onNavigationIconClick,
                id = R.drawable.ic_baseline_arrow_back_24,
                contentDesc = stringResource(id = R.string.back_button)
            )
            /*IconButton(
                onClick = onNavigationIconClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = stringResource(id = R.string.back_button)
                )
            }*/
        },
        actions = {
            if (isDoneIconVisible()) {
                SpecificIconButton(
                    onClick = onDoneIconClick,
                    id = R.drawable.ic_baseline_done_24,
                    contentDesc = stringResource(id = R.string.done_button)
                )
                /*IconButton(
                    onClick = onDoneIconClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_done_24),
                        contentDescription = stringResource(id = R.string.done_button)
                    )
                }*/
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

@OptIn(ExperimentalMaterial3Api::class)
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
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        fontFamily = MaterialTheme.typography.openSansRegular.fontFamily,
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