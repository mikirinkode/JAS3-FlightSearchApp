package com.mikirinkode.flightsearchapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val departureId: Int,
    val departureCode: String,
    val departureName: String,
    val destinationId: Int,
    val destinationCode: String,
    val destinationName: String,
)
