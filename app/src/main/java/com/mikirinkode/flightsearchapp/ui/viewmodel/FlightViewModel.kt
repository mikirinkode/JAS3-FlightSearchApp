package com.mikirinkode.flightsearchapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikirinkode.flightsearchapp.data.FlightRepository
import com.mikirinkode.flightsearchapp.model.Airport
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


    private val _selectedAirport: MutableStateFlow<Airport?> =
        MutableStateFlow(null)
    val selectedAirport get() = _selectedAirport.value



    fun clearQuery() {
        _query.value = ""
    }

    fun insertFavorite(departureCode: String, destinationCode: String){
        viewModelScope.launch {

        }
    }


    fun searchAirport(query: String) {
        _showFlightList.value = false
        _query.value = query
        Log.e(TAG, "query: $query")
        viewModelScope.launch {
            flightRepository.searchAirport(query)
                .catch {  }
                .collect{
                    Log.e(TAG, "search result size: ${it.size}")
                    _searchResultState.value = SearchUiState(it)
                }
        }
    }

    fun showFlightList(airport: Airport){
        _selectedAirport.value = airport
        _showFlightList.value = true
        viewModelScope.launch {
            flightRepository.getAirportList()
                .catch {  }
                .collect{
                    Log.e(TAG, "search result size: ${it.size}")
                    _flightUiState.value = FlightUiState(it)
                }
        }
    }

    companion object {
        private const val TAG = "FlightViewModel"
    }
}

data class SearchUiState(val list: List<Airport> = listOf())
data class FlightUiState(val list: List<Airport> = listOf())