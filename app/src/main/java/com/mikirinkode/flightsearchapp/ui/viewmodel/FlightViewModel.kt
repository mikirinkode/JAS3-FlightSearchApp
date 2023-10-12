package com.mikirinkode.flightsearchapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikirinkode.flightsearchapp.data.FlightRepository
import com.mikirinkode.flightsearchapp.data.UserPreferenceRepository
import com.mikirinkode.flightsearchapp.model.Airport
import com.mikirinkode.flightsearchapp.model.Favorite
import com.mikirinkode.flightsearchapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightViewModel(
    private val flightRepository: FlightRepository,
    private val userPreferenceRepository: UserPreferenceRepository
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


    val lastQueryState: StateFlow<LastQueryUiState> = userPreferenceRepository.userSearchQuery.map {
//        Log.e(TAG, "last query state: $it")
//        if (it != "") {
//            _query.value = it
//            searchAirport(it)
//        }
        LastQueryUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LastQueryUiState()
    )

    fun updateQuery(query: String){
        _query.value = query
    }
    fun clearQuery() {
        _query.value = ""
        viewModelScope.launch {
            userPreferenceRepository.saveUserSearchQuery("")
        }
    }

    init {
        Log.e(TAG, "on init called")
        viewModelScope.launch {
            Log.e(TAG, "last query: ${lastQueryState.value.query}")

            Log.e(TAG, "last query fun: ${userPreferenceRepository.lastQuery}")
            userPreferenceRepository.lastQuery.onEach {
                Log.e(TAG, "aduh ${it}")
            }
        }
        checkLastQuery()
        getFavoriteList()
    }

    fun checkLastQuery() {
        Log.e(TAG, "get check last query called")
        viewModelScope.launch {
            userPreferenceRepository.userSearchQuery.map {
                Log.e(TAG, "last query: $it")
                if (it != "") {
                    _query.value = it
                    searchAirport(it)
                }
            }
        }
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
            // save user query
            Log.e(TAG, "Save query called")
            userPreferenceRepository.saveUserSearchQuery(query.value)

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

data class LastQueryUiState(val query: String = "")
data class SearchUiState(val list: List<Airport> = listOf())
data class FlightUiState(val map: Map<Airport, Boolean> = mapOf<Airport, Boolean>())
data class FavoriteUiState(val list: List<Favorite> = listOf())