package com.example.deslocations.presentation.screens.make_request

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.PlaceRequest
import com.example.deslocations.model.Response
import com.example.deslocations.repository.AuthRepository
import com.example.deslocations.repository.PlacesRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeRequestViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val placesRepo: PlacesRepository
) : ViewModel() {

    var userLocation by mutableStateOf<Location?>(null)

    var makeRequestResponse
            by mutableStateOf<Response<Boolean>>(Response.Success(false))
        private set

    val currentUser: FirebaseUser?
        get() = authRepo.currentUser

    fun makeRequest(placeRequest: PlaceRequest) = viewModelScope.launch {
        makeRequestResponse = Response.Loading
        makeRequestResponse = placesRepo.makeRequest(placeRequest)
    }

}