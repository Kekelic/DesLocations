package com.example.deslocations.presentation.screens.locations_list

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.Place
import com.example.deslocations.model.Response
import com.example.deslocations.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsListViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    var placesResponse
            by mutableStateOf<Response<List<Place>>>(Response.Loading)
        private set

    var userLocation by mutableStateOf<Location?>(null)

    fun saveLocation(location: Location) {
        userLocation = location
    }

    fun getPlaces() = viewModelScope.launch {
        userLocation?.let {
            placesResponse = Response.Loading
            placesResponse = placesRepository.getPlaces(userLocation!!)
        }
    }

    fun searchPlaces(searchText: String) = viewModelScope.launch {
        placesResponse = Response.Loading
        placesResponse = placesRepository.searchPlaces(searchText, userLocation!!)
    }

    fun refreshPlaces() = viewModelScope.launch {
        placesResponse = Response.Loading
        placesResponse = placesRepository.getPlaces(userLocation!!)
    }
}