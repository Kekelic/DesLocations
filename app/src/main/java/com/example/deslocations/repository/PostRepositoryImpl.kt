package com.example.deslocations.repository

import android.net.Uri
import com.example.deslocations.core.Constants.IMAGES
import com.example.deslocations.model.Post
import com.example.deslocations.model.PostType
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.PlaceResponse
import com.example.deslocations.model.response.PostResponse
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore
) : PostRepository {

    override suspend fun makePost(post: Post): Response<Post> {
        return try {
            val postMap = hashMapOf(
                "title" to post.title,
                "description" to post.description,
                "placeID" to post.placeID,
                "authorID" to post.authorID,
                "date" to FieldValue.serverTimestamp(),
                "type" to post.type
            )
            if (post.type == PostType.EVENT) {
                postMap["startDate"] = post.startDate
            }
            post.id = db.collection("Posts").add(postMap).await()
                .id
            Response.Success(post)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getPosts(place: PlaceResponse): Response<PlaceResponse> {
        val posts = ArrayList<PostResponse>()
        return try {
            db.collection("Posts").whereEqualTo("placeID", place.id)
                .orderBy("date", Query.Direction.DESCENDING).get().await().map {
                    val post = it.toObject(PostResponse::class.java)
                    post.id = it.id
                    posts.add(post)
                }
            posts.forEach { post ->
                db.collection("PostImages").whereEqualTo("postID", post.id).limit(1).get().await()
                    .map {
                        post.imageUris = listOf(Uri.parse(it.get("imageUrl").toString()))
                    }
                val documentSnapshot =
                    db.collection("Users").document(post.authorID!!).get().await()
                post.authorName = documentSnapshot.get("firstName")
                    .toString() + " " + documentSnapshot.get("lastName").toString()
                post.isImageRight = Random.nextBoolean()

            }
            place.posts = posts
            Response.Success(place)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getPost(postID: String): Response<PostResponse> {
        return try {
            val postSnapshot = db.collection("Posts").document(postID).get().await()
            val post = postSnapshot.toObject(PostResponse::class.java)
            post?.let {
                it.id = postSnapshot.id
                val imageUris = ArrayList<Uri>()
                db.collection("PostImages").whereEqualTo("postID", it.id).get().await().map {
                    imageUris.add(Uri.parse(it.get("imageUrl").toString()))
                }
                it.imageUris = imageUris
                val userSnapshot = db.collection("Users").document(it.authorID!!).get().await()
                it.authorName =
                    userSnapshot.get("firstName").toString() + " " + userSnapshot.get("lastName")
                        .toString()
            }
            Response.Success(post)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun deletePost(post: PostResponse): Response<String> {
        val placeID = post.placeID
        return try {
            post.imageUris!!.forEach { uri ->
                val imageName = uri.toString().substringAfter("%2F").substringBefore('?')
                storage.reference.child("$IMAGES/$imageName").delete()
            }
            db.collection("PostImages").whereEqualTo("postID", post.id!!).get().await().forEach {
                it.reference.delete()
            }
            db.collection("Posts").document(post.id!!).delete().await()
            Response.Success(placeID)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

}