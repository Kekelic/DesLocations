package com.example.deslocations.model

data class PlaceDeclined(
    var id: String? = null,
    var name: String? = null,
    var category: PlaceCategory? = null,
    var categoryName: String? = null,
    var address: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var adminId: String? = null,
    var reasonForDeclining: String? = null
)