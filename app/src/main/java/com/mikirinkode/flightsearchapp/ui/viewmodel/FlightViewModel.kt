package com.mikirinkode.flightsearchapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikirinkode.flightsearchapp.data.FlightRepository
import com.mikirinkode.flightsearchapp.model.Airport
import com.mikirinkode.flightsearchapp.model.Favorite
import com.mikirinkode.flightsearchapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightViewModel(
    private val flightRepository: FlightRepository
) : ViewModel() {

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    private val _showFlightList = mutableStateOf(false)
    val showFlightList: State<Boolean> get() = _showFlightList

    private val _searchResultState: MutableStateFlow<SearchUiState> =
        MutableStateFlow(SearchUiState())
    val searchResultState get() = _searchResultState


    private val _flightUiState: MutableStateFlow<FlightUiState> =
        MutableStateFlow(FlightUiState())
    val flightUiState get() = _flightUiState


    private val _favoriteUiState: MutableStateFlow<FavoriteUiState> =
        MutableStateFlow(FavoriteUiState())
    val favoriteUiState get() = _favoriteUiState


    private val _selectedAirport: MutableStateFlow<Airport?> =
        MutableStateFlow(null)
    val selectedAirport get() = _selectedAirport.value


    fun clearQuery() {
        _query.value = ""
    }

    init {
        getFavoriteList()
    }

    fun getFavoriteList() {
        viewModelScope.launch {
            flightRepository.getFavoriteList()
                .catch { }
                .collect {
                    _favoriteUiState.value = FavoriteUiState(it)
                }
        }
    }

    fun insertFavorite(departureAirport: Airport, destinationAirport: Airport) {
        viewModelScope.launch {
            flightRepository.insertFavorite(
                Favorite(
                    departureId = departureAirport.id,
                    departureName = departureAirport.name,
                    departureCode = departureAirport.iata_code,
                    destinationId = destinationAirport.id,
                    destinationName = destinationAirport.name,
                    destinationCode = destinationAirport.iata_code
                )
            )
        }
    }

    fun removeFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            flightRepository.removeFavorite(departureCode, destinationCode)
        }
    }


    fun searchAirport(query: String) {
        _showFlightList.value = false
        _query.value = query
        Log.e(TAG, "query: $query")
        viewModelScope.launch {
            flightRepository.searchAirport(query)
                .catch { }
                .collect {
                    Log.e(TAG, "search result size: ${it.size}")
                    _searchResultState.value = SearchUiState(it)
                }
        }
    }

    fun showFlightList(departureAirport: Airport) {
        _selectedAirport.value = departureAirport
        _showFlightList.value = true
        viewModelScope.launch {
            flightRepository.getAirportList()
                .catch { }
                .collect { airportList ->

                    val destinationList = airportList
                    val favoriteList = _favoriteUiState.value.list
                    val result: MutableMap<Airport, Boolean> = mutableMapOf()

                    destinationList.forEach { destinationAirport ->
                        val favoriteItem =
                            favoriteList.firstOrNull { it.departureId == departureAirport.id && it.destinationId == destinationAirport.id }
                        result[destinationAirport] = favoriteItem != null
                    }

                    _flightUiState.value = FlightUiState(result)
                }
        }
    }

    companion object {
        private const val TAG = "FlightViewModel"
    }
}

data class SearchUiState(val list: List<Airport> = listOf())
data class FlightUiState(val map: Map<Airport, Boolean> = mapOf<Airport, Boolean>())
data class FavoriteUiState(val list: List<Favorite> = listOf())