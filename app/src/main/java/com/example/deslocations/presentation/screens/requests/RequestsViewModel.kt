package com.example.deslocations.presentation.screens.requests

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.PlaceRequest
import com.example.deslocations.model.Response
import com.example.deslocations.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestsViewModel @Inject constructor(
    private val placeRepository: PlacesRepository
) : ViewModel() {

    var requestsResponse
            by mutableStateOf<Response<List<PlaceRequest>>>(Response.Loading)
        private set

    var approveRequestResponse
            by mutableStateOf<Response<PlaceRequest>>(Response.Success(null))
        private set

    var declineRequestResponse
            by mutableStateOf<Response<PlaceRequest>>(Response.Success(null))
        private set

    init {
        getPlaceRequests()
    }


    private fun getPlaceRequests() {
        viewModelScope.launch {
            requestsResponse = Response.Loading
            requestsResponse = placeRepository.getRequests()
        }
    }

    fun refreshPlaceRequests() = viewModelScope.launch {
        requestsResponse = Response.Loading
        requestsResponse = placeRepository.getRequests()
    }


    fun approvePlaceRequest(placeRequest: PlaceRequest) {
        viewModelScope.launch {
            approveRequestResponse = Response.Loading
            approveRequestResponse = placeRepository.makePlace(placeRequest)
        }
    }

    fun declinePlaceRequest(placeRequest: PlaceRequest, reasonForDecliningRequest: String) {
        viewModelScope.launch {
            declineRequestResponse = Response.Loading
            declineRequestResponse =
                placeRepository.makePlaceDeclined(placeRequest, reasonForDecliningRequest)
        }
    }

    fun resetRequestsResponse() {
        requestsResponse = Response.Success(null)
    }

    fun resetApproveRequestResponse() {
        approveRequestResponse = Response.Success(null)
    }

    fun resetDeclineRequestResponse() {
        declineRequestResponse = Response.Success(null)
    }


}