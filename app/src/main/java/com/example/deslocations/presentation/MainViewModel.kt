package com.example.deslocations.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.repository.AuthRepository
import com.example.deslocations.repository.PlacesRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    var isModerator by mutableStateOf(false)
        private set

    init {
        getAuthState()
    }

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    fun getAuthState() = authRepository.getAuthState(viewModelScope)

    fun getModeratorState() = viewModelScope.launch {
        isModerator = authRepository.isModerator()
    }

    fun subscribeToFavorites() = viewModelScope.launch {
        placesRepository.subscribeToFavorites(currentUser!!.uid)
    }
}