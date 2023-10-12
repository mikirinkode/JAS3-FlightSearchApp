package com.mikirinkode.flightsearchapp.data

import com.mikirinkode.flightsearchapp.model.Airport
import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    fun searchAirport(query: String): Flow<List<Airport>>
    fun getAirportList(): Flow<List<Airport>>
}

class OfflineFlightRepository(
    private val flightDao: FlightDao
): FlightRepository {
    override fun searchAirport(query: String): Flow<List<Airport>> {
        return flightDao.searchAirport(query)
    }

    override fun getAirportList(): Flow<List<Airport>> {
        return flightDao.getAirportList()
    }

}