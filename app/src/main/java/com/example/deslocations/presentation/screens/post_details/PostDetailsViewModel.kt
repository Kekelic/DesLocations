package com.example.deslocations.presentation.screens.post_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.PostResponse
import com.example.deslocations.repository.AuthRepository
import com.example.deslocations.repository.PlacesRepository
import com.example.deslocations.repository.PostRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val placesRepository: PlacesRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    var getPostResponse
            by mutableStateOf<Response<PostResponse>>(Response.Loading)
        private set

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    var adminID by mutableStateOf("")
        private set

    var deletePostResponse
            by mutableStateOf<Response<String>>(Response.Success(null))
        private set


    fun getAdminState(placeID: String) = viewModelScope.launch {
        adminID = placesRepository.getPlaceAdminId(placeID)
    }

    fun getPost(postID: String) = viewModelScope.launch {
        getPostResponse = Response.Loading
        getPostResponse = postRepository.getPost(postID)
    }

    fun deletePost(post: PostResponse) = viewModelScope.launch {
        deletePostResponse = Response.Loading
        deletePostResponse = postRepository.deletePost(post)
    }


}