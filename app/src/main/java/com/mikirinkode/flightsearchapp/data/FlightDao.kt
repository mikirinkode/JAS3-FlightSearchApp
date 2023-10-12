package com.mikirinkode.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikirinkode.flightsearchapp.model.Airport
import com.mikirinkode.flightsearchapp.model.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

    @Query("DELETE from favorite WHERE departureCode = :departureCode AND destinationCode = :destinationCode")
    suspend fun removeFavorite(departureCode: String, destinationCode: String)

    @Query("SELECT * from favorite")
    fun getFavoriteList(): Flow<List<Favorite>>

    @Query("SELECT * from airport WHERE name LIKE '%'|| :query || '%' OR iata_code LIKE '%'|| :query || '%'")
    fun searchAirport(query: String): Flow<List<Airport>>

//    @Query("SELECT 1 from airport WHERE id LIKE :id")
//    fun getAirportById(id: Int): Flow<Airport>

    @Query("SELECT * FROM airport")
    fun getAirportList(): Flow<List<Airport>>
}