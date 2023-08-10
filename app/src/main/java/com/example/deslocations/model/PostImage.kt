package com.example.deslocations.model

import android.net.Uri

data class PostImage(
    var id: String? = null,
    var imageUrl: Uri? = null,
    var postID: String? = null,
)
