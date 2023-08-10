package com.example.deslocations.model

data class User(
    var firstName: String = "",
    var lastName: String = "",
    var email: String,
    var password: String,
    var isModerator: Boolean = false
)
