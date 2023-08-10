package com.example.deslocations.presentation.screens.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.MessageResponse
import com.example.deslocations.model.response.PlaceResponse
import com.example.deslocations.repository.AuthRepository
import com.example.deslocations.repository.ChatRepository
import com.example.deslocations.repository.PlacesRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val placesRepository: PlacesRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {
    var getPlaceResponse
            by mutableStateOf<Response<PlaceResponse>>(Response.Loading)
        private set

    var sendMessageResponse
            by mutableStateOf<Response<Boolean>>(Response.Success(false))
        private set

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    private val _messagesLiveData = MutableLiveData<List<MessageResponse>>()
    val messagesLiveData: LiveData<List<MessageResponse>>
        get() = _messagesLiveData

    fun getPlace(placeID: String) = viewModelScope.launch {
        getPlaceResponse = Response.Loading
        getPlaceResponse = placesRepository.getPlace(placeID, currentUser!!.uid)
    }

    fun sendMessage(messageContent: String, placeID: String) = viewModelScope.launch {
        sendMessageResponse = Response.Loading
        sendMessageResponse =
            chatRepository.sendMessage(messageContent, placeID, currentUser!!.uid)
    }

    fun getMessages(placeID: String) = viewModelScope.launch {
        chatRepository.getMessages(_messagesLiveData, placeID)
    }


}