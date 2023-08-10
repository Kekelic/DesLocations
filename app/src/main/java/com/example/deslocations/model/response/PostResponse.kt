package com.example.deslocations.model.response

import android.net.Uri
import com.example.deslocations.model.PostType
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class PostResponse(
    var id: String? = null,
    var title: String? = null,
    var type: PostType? = null,
    var startDate: String? = null,
    var description: String? = null,
    var placeID: String? = null,
    var authorID: String? = null,
    var authorName: String? = null,
    @ServerTimestamp
    var date: Date? = null,
    var isImageRight: Boolean? = null,
    var imageUris: List<Uri>? = null,
)
