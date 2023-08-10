package com.example.deslocations.presentation.screens.your_locations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.PlaceDeclined
import com.example.deslocations.model.PlaceRequest
import com.example.deslocations.model.Response
import com.example.deslocations.repository.AuthRepository
import com.example.deslocations.repository.PlacesRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YourLocationsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    var placesResponse
            by mutableStateOf<Response<List<Any>>>(Response.Loading)
        private set

    var deletingPlaceDeclinedResponse
            by mutableStateOf<Response<PlaceDeclined>>(Response.Success(null))
        private set

    var cancelingPlaceRequestResponse
            by mutableStateOf<Response<PlaceRequest>>(Response.Success(null))
        private set

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        getAllUserPlaces()
    }

    private fun getAllUserPlaces() {
        viewModelScope.launch {
            placesResponse = Response.Loading
            placesResponse = placesRepository.getAllUserPlaces(currentUser?.uid ?: "")
        }
    }

    fun refreshUserPlaces() = viewModelScope.launch {
        placesResponse = Response.Loading
        placesResponse = placesRepository.getAllUserPlaces(currentUser?.uid ?: "")
    }

    fun deletePlaceDeclined(placeDeclined: PlaceDeclined) {
        viewModelScope.launch {
            deletingPlaceDeclinedResponse = Response.Loading
            deletingPlaceDeclinedResponse = placesRepository.deletePlaceDeclined(placeDeclined)
        }
    }

    fun cancelPlaceRequest(placeRequest: PlaceRequest) {
        viewModelScope.launch {
            cancelingPlaceRequestResponse = Response.Loading
            cancelingPlaceRequestResponse = placesRepository.cancelPlaceRequest(placeRequest)
        }
    }

    fun resetPlacesResponse() {
        placesResponse = Response.Success(null)
    }

    fun resetDeletingPlaceDeclinedResponse() {
        deletingPlaceDeclinedResponse = Response.Success(null)
    }

    fun resetCancelingPlaceRequestResponse() {
        cancelingPlaceRequestResponse = Response.Success(null)
    }

}