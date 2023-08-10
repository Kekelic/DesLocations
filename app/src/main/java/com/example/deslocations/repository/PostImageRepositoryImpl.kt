package com.example.deslocations.repository

import android.net.Uri
import com.example.deslocations.core.Constants.IMAGES
import com.example.deslocations.model.Post
import com.example.deslocations.model.PostImage
import com.example.deslocations.model.Response
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostImageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore
) : PostImageRepository {

    override suspend fun addImageToDatabase(post: Post): Response<Post> {
        return try {
            val storageRef = storage.reference.child(IMAGES)
            post.imageUris!!.forEach { imageUri ->
                val downloadUrl = storageRef.child(UUID.randomUUID().toString() + ".jpg")
                    .putFile(imageUri).await()
                    .storage.downloadUrl.await()
                val image = hashMapOf(
                    "imageUrl" to downloadUrl,
                    "postID" to post.id,
                )
                db.collection("PostImages").add(image).await()
            }
            Response.Success(post)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getImageFromFirestore(postID: String): Response<List<PostImage>> {
        return try {
            val postImages = ArrayList<PostImage>()
            db.collection("PostImages").whereEqualTo("postID", postID).get().await().map {
                val postImage = it.toObject(PostImage::class.java)
                postImage.id = it.id
                postImages.add(postImage)
            }
            Response.Success(postImages)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getFirstImageFromFirestore(postID: String): Response<PostImage> {
        return try {
            val postImage = PostImage()
            db.collection("PostImages").whereEqualTo("postID", postID).limit(1).get().await().map {
                postImage.postID = it.get("postID").toString()
                postImage.imageUrl = Uri.parse(it.get("imageUrl").toString())
                postImage.id = it.id
            }
            Response.Success(postImage)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}