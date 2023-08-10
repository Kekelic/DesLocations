package com.example.deslocations.presentation.screens.sign_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.Response
import com.example.deslocations.model.User
import com.example.deslocations.repository.AuthRepository
import com.example.deslocations.repository.PlacesRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val placesRepository: PlacesRepository
) : ViewModel() {
    var signInResponse by mutableStateOf<Response<Boolean>>(Response.Success(false))
        private set

    var subscribeToFavoritesResponse by mutableStateOf<Response<Boolean>>(Response.Success(false))
        private set

    var sendPasswordResetEmailResponse by mutableStateOf<Response<Boolean>>(Response.Success(false))

    fun signIn(user: User) = viewModelScope.launch {
        signInResponse = Response.Loading
        signInResponse = authRepository.signIn(user)
    }

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        sendPasswordResetEmailResponse = Response.Loading
        sendPasswordResetEmailResponse = authRepository.sendPasswordResetEmail(email)
    }

    fun subscribeToFavorites() = viewModelScope.launch {
        subscribeToFavoritesResponse = Response.Loading
        subscribeToFavoritesResponse = placesRepository.subscribeToFavorites(currentUser!!.uid)
    }

    fun resetSendPasswordResetEmailResponse() {
        sendPasswordResetEmailResponse = Response.Success(false)
    }

}