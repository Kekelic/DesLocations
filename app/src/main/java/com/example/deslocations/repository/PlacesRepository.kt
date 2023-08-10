package com.example.deslocations.repository

import android.location.Location
import com.example.deslocations.model.Place
import com.example.deslocations.model.PlaceDeclined
import com.example.deslocations.model.PlaceRequest
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.PlaceResponse

interface PlacesRepository {

    suspend fun makeRequest(placeRequest: PlaceRequest): Response<Boolean>

    suspend fun getRequests(): Response<List<PlaceRequest>>

    suspend fun makePlace(placeRequest: PlaceRequest): Response<PlaceRequest>

    suspend fun makePlaceDeclined(
        placeRequest: PlaceRequest,
        reasonForNotApproving: String
    ): Response<PlaceRequest>

    suspend fun getAllUserPlaces(userID: String): Response<List<Any>>

    suspend fun deletePlaceDeclined(placeDeclined: PlaceDeclined): Response<PlaceDeclined>

    suspend fun cancelPlaceRequest(placeRequest: PlaceRequest): Response<PlaceRequest>

    suspend fun getPlace(placeID: String, userID: String): Response<PlaceResponse>

    suspend fun updateDescription(placeID: String, description: String): Response<String>

    suspend fun makeFavorite(placeID: String, userID: String): Response<Boolean>

    suspend fun deleteFavorite(placeID: String, userID: String): Response<Boolean>

    suspend fun getPlaceAdminId(placeID: String): String

    suspend fun getPlaces(location: Location): Response<List<Place>>

    suspend fun searchPlaces(searchText: String, location: Location): Response<List<Place>>

    suspend fun getPlaceName(placeID: String): Response<String>

    suspend fun subscribeToFavorites(userID: String): Response<Boolean>

    suspend fun unsubscribeFromFavorites(userID: String): Response<Boolean>


}