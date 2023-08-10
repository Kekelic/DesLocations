package com.example.deslocations.repository

import com.example.deslocations.model.Post
import com.example.deslocations.model.PostImage
import com.example.deslocations.model.Response

interface PostImageRepository {

    suspend fun addImageToDatabase(post: Post): Response<Post>

    suspend fun getImageFromFirestore(postID: String): Response<List<PostImage>>

    suspend fun getFirstImageFromFirestore(postID: String): Response<PostImage>
}