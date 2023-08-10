package com.example.deslocations.repository

import com.example.deslocations.model.Response
import com.example.deslocations.model.User
import com.example.deslocations.model.response.UserResponse
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    val currentUser: FirebaseUser?
    suspend fun signIn(user: User): Response<Boolean>
    suspend fun signUp(user: User): Response<Boolean>
    fun signOut()
    fun getAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean>
    suspend fun isModerator(): Boolean
    suspend fun getUserDetails(): Response<UserResponse>
    suspend fun sendPasswordResetEmail(email: String): Response<Boolean>
    suspend fun changeAccountDetails(user: User): Response<Boolean>

}