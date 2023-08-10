package com.example.deslocations.model.response

import com.example.deslocations.model.PlaceCategory

data class PlaceResponse(
    var id: String? = null,
    var name: String? = null,
    var category: PlaceCategory? = null,
    var categoryName: String? = null,
    var address: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var adminId: String? = null,
    var description: String? = null,
    var isFavorite: Boolean = false,
    var posts: ArrayList<PostResponse> = ArrayList()
)
