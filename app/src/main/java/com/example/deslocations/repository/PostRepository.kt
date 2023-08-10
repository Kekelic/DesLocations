package com.example.deslocations.repository

import com.example.deslocations.model.Post
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.PlaceResponse
import com.example.deslocations.model.response.PostResponse

interface PostRepository {

    suspend fun makePost(post: Post): Response<Post>

    suspend fun getPosts(place: PlaceResponse): Response<PlaceResponse>

    suspend fun getPost(postID: String): Response<PostResponse>

    suspend fun deletePost(post: PostResponse): Response<String>
}