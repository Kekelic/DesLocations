package com.example.deslocations.model

import android.net.Uri
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Post(
    var id: String? = null,
    var title: String? = null,
    var type: PostType? = null,
    var startDate: String? = null,
    var description: String? = null,
    var placeID: String? = null,
    var authorID: String? = null,
    @ServerTimestamp
    var date: Date? = null,
    var imageUris: List<Uri>? = null,
)