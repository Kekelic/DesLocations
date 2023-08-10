package com.example.deslocations.presentation.screens.place_details


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.PlaceResponse
import com.example.deslocations.repository.AuthRepository
import com.example.deslocations.repository.PlacesRepository
import com.example.deslocations.repository.PostRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository,

    ) : ViewModel() {

    var updateDescriptionResponse
            by mutableStateOf<Response<String>>(Response.Success(""))
        private set

    var changeFavoriteStateResponse
            by mutableStateOf<Response<Boolean>>(Response.Success(false))


    var getPlaceDetailsResponse
            by mutableStateOf<Response<PlaceResponse>>(Response.Loading)
        private set

    var getPostsResponse
            by mutableStateOf<Response<PlaceResponse>>(Response.Loading)
        private set

    var refreshPostsResponse
            by mutableStateOf<Response<PlaceResponse>>(Response.Success(null))
        private set


    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    fun getPlace(placeID: String) = viewModelScope.launch {
        getPlaceDetailsResponse = Response.Loading
        getPlaceDetailsResponse = placesRepository.getPlace(placeID, currentUser!!.uid)

    }

    fun getPosts(place: PlaceResponse) = viewModelScope.launch {
        getPostsResponse = Response.Loading
        getPostsResponse = postRepository.getPosts(place)
    }

    fun refreshPosts(place: PlaceResponse) = viewModelScope.launch {
        refreshPostsResponse = Response.Loading
        refreshPostsResponse = postRepository.getPosts(place)
    }

    fun updatePlaceDescription(placeID: String, description: String) = viewModelScope.launch {
        updateDescriptionResponse = Response.Loading
        updateDescriptionResponse = placesRepository.updateDescription(placeID, description)
    }

    fun makeFavorite(placeID: String) = viewModelScope.launch {
        changeFavoriteStateResponse = Response.Loading
        changeFavoriteStateResponse = placesRepository.makeFavorite(placeID, currentUser!!.uid)
    }

    fun deleteFavorite(placeID: String) = viewModelScope.launch {
        changeFavoriteStateResponse = Response.Loading
        changeFavoriteStateResponse = placesRepository.deleteFavorite(placeID, currentUser!!.uid)
    }


}