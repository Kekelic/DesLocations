package com.example.deslocations.presentation.screens.account_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.Response
import com.example.deslocations.model.User
import com.example.deslocations.model.response.UserResponse
import com.example.deslocations.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDetailsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    var getUserDataResponse by mutableStateOf<Response<UserResponse>>(Response.Loading)
        private set

    var sendPasswordResetEmailResponse by mutableStateOf<Response<Boolean>>(Response.Success(false))

    var changeAccountDetailsResponse by mutableStateOf<Response<Boolean>>(Response.Success(false))

    init {
        getUserData()
    }

    fun getUserData() = viewModelScope.launch {
        getUserDataResponse = Response.Loading
        getUserDataResponse = authRepository.getUserDetails()
    }

    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        sendPasswordResetEmailResponse = Response.Loading
        sendPasswordResetEmailResponse = authRepository.sendPasswordResetEmail(email)
    }

    fun changeAccountDetails(user: User) = viewModelScope.launch {
        changeAccountDetailsResponse = Response.Loading
        changeAccountDetailsResponse = authRepository.changeAccountDetails(user)
    }

    fun resetSendPasswordResetEmailResponse() {
        sendPasswordResetEmailResponse = Response.Success(false)
    }

    fun resetChangeAccountDetailsResponse() {
        changeAccountDetailsResponse = Response.Success(false)
    }
}