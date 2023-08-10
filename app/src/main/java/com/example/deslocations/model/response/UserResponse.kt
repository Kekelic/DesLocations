package com.example.deslocations.model.response

data class UserResponse(
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var isModerator: Boolean = false
)