package com.mikirinkode.flightsearchapp.data

import com.mikirinkode.flightsearchapp.model.Airport
import com.mikirinkode.flightsearchapp.model.Favorite
import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    fun searchAirport(query: String): Flow<List<Airport>>
    fun getAirportList(): Flow<List<Airport>>
    suspend fun insertFavorite(favorite: Favorite)
    suspend fun removeFavorite(departureCode: String, destinationCode: String)
    fun getFavoriteList(): Flow<List<Favorite>>
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

    override suspend fun insertFavorite(favorite: Favorite) {
        return flightDao.insertFavorite(favorite)
    }

    override suspend fun removeFavorite(departureCode: String, destinationCode: String) {
        return flightDao.removeFavorite(departureCode, destinationCode)
    }

    override fun getFavoriteList(): Flow<List<Favorite>> {
        return flightDao.getFavoriteList()
    }

}