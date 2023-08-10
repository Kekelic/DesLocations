package com.example.deslocations.model.response

import com.google.firebase.Timestamp

data class MessageResponse(
    var id: String? = null,
    var content: String? = null,
    var authorID: String? = null,
    var authorName: String? = null,
    var date: Timestamp? = null,
)