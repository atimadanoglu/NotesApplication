package com.atakanmadanoglu.notesapplication.components.mainpage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        var searchBarText by remember { mutableStateOf("") }

        TextField(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(5.dp, CircleShape),
            value = searchBarText,
            onValueChange = { changedValue ->
                searchBarText = changedValue
            },
            singleLine = true,
            placeholder = { Text(text = "Search notes") }
        )
    }
}

@Preview
@Composable
fun PreviewSearchBar() {
    SearchBar()
}