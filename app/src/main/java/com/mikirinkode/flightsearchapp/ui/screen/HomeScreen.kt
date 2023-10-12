package com.mikirinkode.flightsearchapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikirinkode.flightsearchapp.model.Airport
import com.mikirinkode.flightsearchapp.model.Favorite
import com.mikirinkode.flightsearchapp.ui.theme.FlightSearchAppTheme
import com.mikirinkode.flightsearchapp.ui.viewmodel.AppViewModelProvider
import com.mikirinkode.flightsearchapp.ui.viewmodel.FlightViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: FlightViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val query by viewModel.query
    val showFlightList by viewModel.showFlightList
    val searchState by viewModel.searchResultState.collectAsState()
    val flightUiState by viewModel.flightUiState.collectAsState()
    val favoriteUiState by viewModel.favoriteUiState.collectAsState()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
    ) {
        SearchBar(
            focusManager = focusManager,
            inputValue = query,
            onValueChange = viewModel::searchAirport,
            onClearInput = viewModel::clearQuery
        )
        if (query == "") {
            Text(text = "Favorite Routes", modifier = Modifier.padding(16.dp))
            viewModel.getFavoriteList()
            if (favoriteUiState.list.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "No Favorite Routes Data.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                FavoriteFlightList(list = favoriteUiState.list, onFavoriteClicked = { favorite ->
                    viewModel.removeFavorite(
                        departureCode = favorite.departureCode,
                        destinationCode = favorite.destinationCode
                    )
                    viewModel.getFavoriteList()
                })
            }
        } else {
            if (!showFlightList) {
                SearchResultList(list = searchState.list, onAirportClicked = {
                    focusManager.clearFocus()
                    viewModel.showFlightList(it)
                })
            } else {
                val selectedAirport = viewModel.selectedAirport
                if (selectedAirport != null) {
                    Column {
                        Row {
                            Text(
                                text = "Flights From ${selectedAirport?.iata_code}: ",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        FlightList(
                            selectedAirport = selectedAirport,
                            map = flightUiState.map,
                            onFavoriteClicked = { destinationAirport, isFavorite ->

                                if (isFavorite) {
                                    viewModel.removeFavorite(
                                        departureCode = selectedAirport.iata_code,
                                        destinationCode = destinationAirport.iata_code
                                    )
                                } else {
                                    viewModel.insertFavorite(
                                        departureAirport = selectedAirport,
                                        destinationAirport = destinationAirport
                                    )
                                }
                                viewModel.getFavoriteList()
                                viewModel.showFlightList(selectedAirport)
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultList(
    list: List<Airport>,
    onAirportClicked: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        if (list.isEmpty()) {
            Text("No match data", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(list) { airport ->
                    AirportItem(
                        iataCode = airport.iata_code,
                        name = airport.name,
                        onAirportClicked = {
                            onAirportClicked(airport)
                        })
                }
            }
        }
    }
}

@Composable
fun AirportItem(
    iataCode: String,
    name: String,
    onAirportClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .clip(shape = MaterialTheme.shapes.medium)
                .clickable(onClick = onAirportClicked)
                .padding(8.dp)
        ) {
            Text(iataCode, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(name)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    focusManager: FocusManager,
    inputValue: String,
    onValueChange: (String) -> Unit,
    onClearInput: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                IconButton(onClick = {
                    onClearInput()
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Close Button",
                    )
                }
            }
        },
        placeholder = {
            Text(text = "Search an Airport")
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
                shape = MaterialTheme.shapes.medium
            )
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus() },
            onDone = { focusManager.clearFocus() })
    )
}

@Composable
fun FavoriteFlightList(
    list: List<Favorite>,
    onFavoriteClicked: (Favorite) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = modifier
    ) {
        items(list) { favorite ->
            FlightItemCard(
                departureCode = favorite.departureCode,
                departureName = favorite.departureName,
                arriveCode = favorite.destinationCode,
                arriveName = favorite.destinationName,
                isFavorite = true,
                onFavoriteClicked = { onFavoriteClicked(favorite) }
            )
        }
    }
}

@Composable
fun FlightList(
    selectedAirport: Airport,
    map: Map<Airport, Boolean>,
    onFavoriteClicked: (destinationAirport: Airport, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = modifier
    ) {
        items(map.keys.toList()) { airport ->
            var isFavorite by remember {
                mutableStateOf(map[airport] ?: false)
            }
            FlightItemCard(
                departureCode = selectedAirport.iata_code,
                departureName = selectedAirport.name,
                arriveCode = airport.iata_code,
                arriveName = airport.name,
                isFavorite = isFavorite,
                onFavoriteClicked = {
                    onFavoriteClicked(airport, isFavorite)
                    isFavorite = !isFavorite
                })
        }
    }
}

@Composable
fun FlightItemCard(
    isFavorite: Boolean,
    departureCode: String,
    departureName: String,
    arriveCode: String,
    arriveName: String,
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
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Depart", color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(text = departureCode, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = departureName)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Arrive", color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(text = arriveCode, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = arriveName)
                }
            }
            IconButton(onClick = {
                onFavoriteClicked()
            }) {
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