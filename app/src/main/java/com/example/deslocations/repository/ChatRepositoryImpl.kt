package com.example.deslocations.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.MessageResponse
import com.example.deslocations.model.response.UserResponse
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ChatRepository {

    override suspend fun sendMessage(
        messageContent: String,
        placeID: String,
        authorID: String
    ): Response<Boolean> {
        return try {
            val user: UserResponse
            val userSnapshot = db.collection("Users").document(authorID).get().await()
            user = userSnapshot.toObject(UserResponse::class.java)!!

            val chatPlaceRef = db.collection("Chats").document(placeID)
            val userSenderSnapshot =
                chatPlaceRef.collection("UsersSenders").whereEqualTo("userID", authorID).get()
                    .await()
            if (userSenderSnapshot == null || userSenderSnapshot.isEmpty) {
                val userSender = hashMapOf(
                    "userID" to authorID,
                )
                chatPlaceRef.collection("UsersSenders").add(userSender).await()
            }

            val message = hashMapOf(
                "content" to messageContent,
                "authorID" to authorID,
                "authorName" to "${user.firstName} ${user.lastName}",
                "date" to Timestamp(Date())
            )
            chatPlaceRef.collection("Messages").add(message).await()

            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


    override fun getMessages(
        liveDataMessages: MutableLiveData<List<MessageResponse>>,
        placeID: String
    ) {
        db.collection("Chats").document(placeID).collection("Messages").orderBy("date")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("CHAT ERROR: ", "Listen failed. ", error)
                    return@addSnapshotListener
                }
                val messages = ArrayList<MessageResponse>()
                if (value != null) {
                    for (doc in value) {
                        val message = doc.toObject(MessageResponse::class.java)
                        message.id = doc.id
                        db.collection("Users").document(message.authorID!!).get()
                            .addOnSuccessListener {
                                val user = it.toObject(UserResponse::class.java)
                                message.authorName = "${user!!.firstName} ${user.lastName}"
                            }
                        messages.add(message)
                    }
                }
                liveDataMessages.postValue(messages)
            }
    }

}