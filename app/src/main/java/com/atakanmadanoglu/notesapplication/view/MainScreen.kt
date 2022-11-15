package com.atakanmadanoglu.notesapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.model.Note
import com.atakanmadanoglu.notesapplication.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium),
        floatingActionButton = { Fab() }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ) {
            AllNotesText()
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            TotalNotesCount()
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            SearchBar()
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            val list = listOf(
                Note(1,"Hello", "there" , 1L)
            )
            NotesListView(notes = list)
        }
    }
}

@Composable
private fun AllNotesText(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.all_notes),
        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(top = MaterialTheme.spacing.large)
    )
}

@Composable
private fun TotalNotesCount(
    notesCount: Int = 0
) {
    val totalNotesCount by remember {
        mutableStateOf(notesCount)
    }
    val totalNotes = stringResource(id = R.string.total_notes_count, totalNotesCount)
    Text(
        text = totalNotes,
        fontSize = MaterialTheme.typography.headlineSmall.fontSize
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchValue: String = ""
) {
    var searchBarText by remember { mutableStateOf(searchValue) }
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(48.dp)),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_search_24),
                contentDescription = stringResource(id = R.string.search_icon)
            ) },
        value = searchBarText,
        onValueChange = { changedValue ->
            searchBarText = changedValue
        },
        textStyle = TextStyle(
            fontSize = 16.sp
        ),
        singleLine = true,
        placeholder = { Text(
            text = stringResource(id = R.string.search_notes),
            fontSize = 16.sp
        ) },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}

@Preview
@Composable
fun PreviewSearchBar() {
    SearchBar()
}

@Composable
fun NotesListView(notes: List<Note>) {
    LazyColumn(
        contentPadding = PaddingValues(MaterialTheme.spacing.extraSmall)
    ) {
        items(notes) { note ->
            NoteRow(note = note)
        }
    }
}

@Composable
private fun NoteRow(
    note: Note,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = MaterialTheme.spacing.extraSmall)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = note.title,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                maxLines = 1
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Todo", /*TODO("Implement a function that converts the long value to a date string format")*/
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1
                )
                Text(
                    text = stringResource(id = R.string.vertical_line),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
                Text(
                    text = note.description,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun Fab(onClick: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = stringResource(id = R.string.add_note),
            tint = Color.White
        )
    }
}

@Preview
@Composable
fun Preview() {
    NoteRow(note = Note(1,"Hello", "There", 1))
}