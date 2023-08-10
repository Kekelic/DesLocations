package com.example.deslocations.repository

import androidx.lifecycle.MutableLiveData
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.MessageResponse

interface ChatRepository {

    suspend fun sendMessage(
        messageContent: String,
        placeID: String,
        authorID: String
    ): Response<Boolean>

    fun getMessages(liveDataMessages: MutableLiveData<List<MessageResponse>>, placeID: String)

}