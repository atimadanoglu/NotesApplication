package com.atakanmadanoglu.notesapplication.view

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
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.ui.theme.spacing
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.small),
        topBar = { NavigationTopAppBar() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TitleInput()
            ShowDate()
            NoteContentView()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationTopAppBar() {
    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                contentDescription = stringResource(id = R.string.back_button)
            )
        },
        actions = {
            IconButton(
                onClick = { /*TODO*/ }
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
fun TitleInput(
    modifier: Modifier = Modifier
) {
    var titleValue by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent),
        value = titleValue,
        onValueChange = { titleValue = it },
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
fun NoteContentView() {
    var titleValue by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        value = titleValue,
        onValueChange = { titleValue = it },
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
fun ShowDate() {
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
fun PreviewAddNoteScreenComponents() {
    NavigationTopAppBar()
}