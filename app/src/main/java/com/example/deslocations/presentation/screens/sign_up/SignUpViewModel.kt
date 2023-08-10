package com.example.deslocations.presentation.screens.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.Response
import com.example.deslocations.model.User
import com.example.deslocations.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {
    var signUpResponse by mutableStateOf<Response<Boolean>>(Response.Success(false))
        private set

    fun signUp(
        user: User
    ) = viewModelScope.launch {
        signUpResponse = Response.Loading
        signUpResponse = repo.signUp(user)
    }

    val currentUser: FirebaseUser?
        get() = repo.currentUser
}