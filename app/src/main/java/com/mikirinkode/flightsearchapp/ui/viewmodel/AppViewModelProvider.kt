package com.mikirinkode.flightsearchapp.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mikirinkode.flightsearchapp.BaseApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FlightViewModel(
                baseApplication().container.flightRepository
            )
        }
    }
}

fun CreationExtras.baseApplication(): BaseApplication {
    return (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BaseApplication)
}