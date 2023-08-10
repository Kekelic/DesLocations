package com.example.deslocations.presentation.screens.make_post

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.core.Constants
import com.example.deslocations.model.Post
import com.example.deslocations.model.PushNotification
import com.example.deslocations.model.Response
import com.example.deslocations.repository.AuthRepository
import com.example.deslocations.repository.NotificationRetrofitInstance
import com.example.deslocations.repository.PlacesRepository
import com.example.deslocations.repository.PostImageRepository
import com.example.deslocations.repository.PostRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakePostViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository,
    private val postImageRepository: PostImageRepository,
    private val placesRepository: PlacesRepository,
) : ViewModel() {


    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    var currentPost by mutableStateOf<Post?>(null)

    var addPostToDatabaseResponse by mutableStateOf<Response<Post>>(Response.Success(null))
        private set

    var addImageToDatabaseResponse by mutableStateOf<Response<Post>>(Response.Success(null))
        private set

    var getPlaceNameResponse by mutableStateOf<Response<String>>(Response.Success(null))
        private set


    fun makePost(post: Post) = viewModelScope.launch {
        addPostToDatabaseResponse = Response.Loading
        post.authorID = currentUser!!.uid
        addPostToDatabaseResponse = postRepository.makePost(post)
    }

    fun addImageToDatabase(post: Post) = viewModelScope.launch {
        addImageToDatabaseResponse = Response.Loading
        addImageToDatabaseResponse = postImageRepository.addImageToDatabase(post)
    }

    fun getPlaceName(placeID: String) = viewModelScope.launch {
        getPlaceNameResponse = Response.Loading
        getPlaceNameResponse = placesRepository.getPlaceName(placeID)
    }

    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            NotificationRetrofitInstance.api.postNotification(notification)
        } catch (e: Exception) {
            Log.e(Constants.ERROR_TAG, e.message.toString())
        }

    }


}