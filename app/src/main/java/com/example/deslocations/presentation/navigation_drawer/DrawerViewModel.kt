package com.example.deslocations.presentation.navigation_drawer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.Response
import com.example.deslocations.repository.AuthRepository
import com.example.deslocations.repository.PlacesRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    var unsubscribeFromFavoritesResponse by mutableStateOf<Response<Boolean>>(Response.Success(false))


    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    fun signOut() = viewModelScope.launch { authRepository.signOut() }


    fun unsubscribeFromFavorites() = viewModelScope.launch {
        unsubscribeFromFavoritesResponse = Response.Loading
        unsubscribeFromFavoritesResponse =
            placesRepository.unsubscribeFromFavorites(currentUser!!.uid)
    }


}