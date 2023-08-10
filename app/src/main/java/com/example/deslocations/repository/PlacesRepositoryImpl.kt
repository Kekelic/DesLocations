package com.example.deslocations.repository

import android.location.Location
import android.text.TextUtils
import com.example.deslocations.model.Place
import com.example.deslocations.model.PlaceDeclined
import com.example.deslocations.model.PlaceRequest
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.Favorite
import com.example.deslocations.model.response.PlaceResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firebaseMessaging: FirebaseMessaging
) : PlacesRepository {

    override suspend fun makeRequest(placeRequest: PlaceRequest): Response<Boolean> {
        return try {
            val placeRequest = hashMapOf(
                "name" to placeRequest.name,
                "category" to placeRequest.category,
                "categoryName" to placeRequest.categoryName,
                "adminRole" to placeRequest.adminRole,
                "reason" to placeRequest.reason,
                "address" to placeRequest.address,
                "latitude" to placeRequest.latitude,
                "longitude" to placeRequest.longitude,
                "adminId" to placeRequest.adminId,
            )
            db.collection("PlaceRequests").add(placeRequest).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getRequests(): Response<List<PlaceRequest>> {
        return try {
            val requests = ArrayList<PlaceRequest>()
            db.collection("PlaceRequests").get().await().map {
                val placeRequest = it.toObject(PlaceRequest::class.java)
                placeRequest.id = it.id
                requests.add(placeRequest)
            }
            Response.Success(requests)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun makePlace(placeRequest: PlaceRequest): Response<PlaceRequest> {
        return try {
            val place = hashMapOf(
                "name" to placeRequest.name,
                "category" to placeRequest.category,
                "categoryName" to placeRequest.categoryName,
                "address" to placeRequest.address,
                "latitude" to placeRequest.latitude,
                "longitude" to placeRequest.longitude,
                "adminId" to placeRequest.adminId,
                "description" to "",
            )
            db.collection("Places").add(place).await()
            db.collection("PlaceRequests").document(placeRequest.id!!).delete().await()
            Response.Success(placeRequest)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun makePlaceDeclined(
        placeRequest: PlaceRequest,
        reasonForNotApproving: String
    ): Response<PlaceRequest> {
        return try {
            val placeDeclined = hashMapOf(
                "name" to placeRequest.name,
                "category" to placeRequest.category,
                "categoryName" to placeRequest.categoryName,
                "address" to placeRequest.address,
                "latitude" to placeRequest.latitude,
                "longitude" to placeRequest.longitude,
                "adminId" to placeRequest.adminId,
                "reasonForDeclining" to reasonForNotApproving
            )
            db.collection("PlaceDeclined").add(placeDeclined).await()
            db.collection("PlaceRequests").document(placeRequest.id!!).delete().await()
            Response.Success(placeRequest)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getAllUserPlaces(userID: String): Response<List<Any>> {
        return try {
            val requests = ArrayList<Any>()
            db.collection("PlaceDeclined").whereEqualTo("adminId", userID).get().await().map {
                val placeDeclined = it.toObject(PlaceDeclined::class.java)
                placeDeclined.id = it.id
                requests.add(placeDeclined)
            }
            db.collection("PlaceRequests").whereEqualTo("adminId", userID).get().await().map {
                val placeRequest = it.toObject(PlaceRequest::class.java)
                placeRequest.id = it.id
                requests.add(placeRequest)
            }
            db.collection("Places").whereEqualTo("adminId", userID).get().await().map {
                val place = it.toObject(Place::class.java)
                place.id = it.id
                requests.add(place)
            }
            Response.Success(requests)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun deletePlaceDeclined(placeDeclined: PlaceDeclined): Response<PlaceDeclined> {
        return try {
            db.collection("PlaceDeclined").document(placeDeclined.id!!).delete().await()
            Response.Success(placeDeclined)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun cancelPlaceRequest(placeRequest: PlaceRequest): Response<PlaceRequest> {
        return try {
            db.collection("PlaceRequests").document(placeRequest.id!!).delete().await()
            Response.Success(placeRequest)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getPlace(placeID: String, userID: String): Response<PlaceResponse> {
        return try {
            val place: PlaceResponse
            val documentSnapshot = db.collection("Places").document(placeID).get().await()
            place = documentSnapshot.toObject(PlaceResponse::class.java)!!
            place.id = documentSnapshot.id
            val query = db.collection("Favorites").whereEqualTo("placeID", placeID)
                .whereEqualTo("userID", userID)
            place.isFavorite = !(query.get().await().isEmpty)
            Response.Success(place)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    }

    override suspend fun updateDescription(placeID: String, description: String): Response<String> {
        return try {
            val documentRef = db.collection("Places").document(placeID)
            documentRef.update("description", description).await()
            Response.Success(description)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


    override suspend fun makeFavorite(placeID: String, userID: String): Response<Boolean> {
        return try {
            val favorite = hashMapOf(
                "placeID" to placeID,
                "userID" to userID
            )
            db.collection("Favorites").add(favorite).await()
            firebaseMessaging.subscribeToTopic(placeID).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun deleteFavorite(placeID: String, userID: String): Response<Boolean> {
        return try {
            val query = db.collection("Favorites").whereEqualTo("placeID", placeID)
                .whereEqualTo("userID", userID)
            val documentSnapshot = query.get().await()
            documentSnapshot.forEach { doc ->
                doc.reference.delete()
            }
            firebaseMessaging.unsubscribeFromTopic(placeID).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getPlaceAdminId(placeID: String): String {
        return try {
            val place: PlaceResponse
            val documentSnapshot = db.collection("Places").document(placeID).get().await()
            place = documentSnapshot.toObject(PlaceResponse::class.java)!!
            place.adminId!!
        } catch (e: Exception) {
            ""
        }
    }

    override suspend fun getPlaces(location: Location): Response<List<Place>> {
        return try {
            val places = ArrayList<Place>()
            db.collection("Places")
                .get().await().map {
                    val place = it.toObject(Place::class.java)
                    place.id = it.id
                    val results = FloatArray(1)
                    Location.distanceBetween(
                        location.latitude,
                        location.longitude,
                        place.latitude!!.toDouble(),
                        place.longitude!!.toDouble(),
                        results
                    )
                    place.distance = "%.3f".format(Locale.ENGLISH, results[0] / 1000).toFloat()
                    places.add(place)
                }
            places.sortBy { it.distance }
            Response.Success(places)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun searchPlaces(
        searchText: String,
        location: Location
    ): Response<List<Place>> {
        return if (TextUtils.isEmpty(searchText.trim())) {
            getPlaces(location)
        } else {
            return try {
                val places = ArrayList<Place>()
                db.collection("Places").whereGreaterThanOrEqualTo("name", searchText)
                    .whereLessThanOrEqualTo("name", searchText + '\uf8ff').limit(50)
                    .get().await().map {
                        val place = it.toObject(Place::class.java)
                        place.id = it.id
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            location.latitude,
                            location.longitude,
                            place.latitude!!.toDouble(),
                            place.longitude!!.toDouble(),
                            results
                        )
                        place.distance = "%.3f".format(Locale.ENGLISH, results[0] / 1000).toFloat()
                        places.add(place)
                    }
                db.collection("Places").whereGreaterThanOrEqualTo("category", searchText.uppercase())
                    .whereLessThanOrEqualTo("category", searchText.uppercase() + '\uf8ff').limit(50)
                    .get().await().map {
                        val place = it.toObject(Place::class.java)
                        place.id = it.id
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            location.latitude,
                            location.longitude,
                            place.latitude!!.toDouble(),
                            place.longitude!!.toDouble(),
                            results
                        )
                        place.distance = "%.3f".format(Locale.ENGLISH, results[0] / 1000).toFloat()
                        places.add(place)
                    }
                places.sortBy { it.distance }
                Response.Success(places.distinct())
            } catch (e: Exception) {
                Response.Failure(e)
            }
        }

    }

    override suspend fun getPlaceName(placeID: String): Response<String> {
        return try {
            val place: PlaceResponse
            val documentSnapshot = db.collection("Places").document(placeID).get().await()
            place = documentSnapshot.toObject(PlaceResponse::class.java)!!
            Response.Success(place.name!!)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun subscribeToFavorites(userID: String): Response<Boolean> {
        return try {
            val query = db.collection("Favorites").whereEqualTo("userID", userID)
            val documentSnapshot = query.get().await()
            documentSnapshot.forEach { doc ->
                val favorite = doc.toObject(Favorite::class.java)
                firebaseMessaging.subscribeToTopic(favorite.placeID!!).await()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun unsubscribeFromFavorites(userID: String): Response<Boolean> {
        return try {
            val query = db.collection("Favorites").whereEqualTo("userID", userID)
            val documentSnapshot = query.get().await()
            documentSnapshot.forEach { doc ->
                val favorite = doc.toObject(Favorite::class.java)
                firebaseMessaging.unsubscribeFromTopic(favorite.placeID!!).await()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

}