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
import androidx.compose.ui.tooling.preview.Preview
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.ui.theme.spacing

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
                .fillMaxWidth()
                .padding(it)
        ) {
            TitleInput()
            Divider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopAppBar() {
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
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        ),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun Divider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    )
}

@Preview
@Composable
fun PreviewAddNoteScreenComponents() {
    NavigationTopAppBar()
}