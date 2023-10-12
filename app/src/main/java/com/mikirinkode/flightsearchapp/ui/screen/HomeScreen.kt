package com.mikirinkode.flightsearchapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikirinkode.flightsearchapp.ui.theme.FlightSearchAppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var favoriteList = emptyList<String>()
    var flightList = listOf("", "", "", "")
    Column(
        modifier = modifier
    ) {
        SearchBar(
            inputValue = query,
            onValueChange = { newValue -> query = newValue },
            onClearInput = { query = "" })
        if (query == "") {
            Text(text = "Favorite Routes", modifier = Modifier.padding(16.dp))
            if (favoriteList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "No Favorite Routes Data.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                FavoriteFlightList(list = favoriteList, onFavoriteClicked = {

                })
            }
        } else {
            FlightList(flightList, onFavoriteClicked = {

            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    inputValue: String,
    onValueChange: (String) -> Unit,
    onClearInput: () -> Unit,
    modifier: Modifier = Modifier
) {

    val focusRequester = remember { FocusRequester() }
    TextField(
        value = inputValue,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (inputValue.isNotEmpty()) {
                IconButton(onClick = onClearInput) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Close Button",
                    )
                }
            }
        },
        placeholder = {
            Text(text = "Search")
        },
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        modifier = modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
            )
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
            )
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusRequester.freeFocus() })
    )
}

@Composable
fun FavoriteFlightList(
    list: List<String>,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(list) {
                FlightItemCard(
                    isFavorite = true,
                    onFavoriteClicked = onFavoriteClicked,
                )
        }
    }
}

@Composable
fun FlightList(
    list: List<String>,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = Modifier
    ) {
        items(list) {
            FlightItemCard(onFavoriteClicked = onFavoriteClicked)
        }
    }
}

@Composable
fun FlightItemCard(
    isFavorite: Boolean = false,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .background(
                color = if (isFavorite) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Gray
                },
                shape = CutCornerShape(bottomEnd = 16.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
            )
//            .border(
//                width = 2.dp,
//                shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
//                color = MaterialTheme.colorScheme.background
//            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Depart")
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(text = "FCO", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Airport")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Arrive")
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(text = "FCO", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Airport")
                }
            }
            IconButton(onClick = onFavoriteClicked) {
                Icon(
                    imageVector = if (isFavorite) {
                        Icons.Sharp.Star
                    } else {
                        Icons.Outlined.Star
                    },
                    contentDescription = "Star Icon",
                    tint = if (isFavorite) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Gray
                    }
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview() {
    FlightSearchAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen()
        }
    }
}