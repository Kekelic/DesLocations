package com.example.deslocations.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.deslocations.model.Place
import com.example.deslocations.model.PlaceCategory
import com.example.deslocations.model.PlaceDeclined
import com.example.deslocations.model.PlaceRequest

@Composable
fun <T : Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
    return rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                if (stateList.isNotEmpty()) {
                    val first = stateList.first()
                    if (!canBeSaved(first)) {
                        throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                    }
                }
                stateList.toList()
            },
            restore = { it.toMutableStateList() }
        )
    ) {
        elements.toList().toMutableStateList()
    }
}


@Composable
fun rememberMutableStateListOfPlace(vararg elements: Place): SnapshotStateList<Place> {
    return rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                val saveList = ArrayList<List<Any?>>()
                stateList.forEach { place ->
                    saveList.add(
                        listOf(
                            place.id,
                            place.name,
                            place.category,
                            place.address,
                            place.latitude,
                            place.longitude,
                            place.adminId,
                            place.description,
                            place.isFavorite,
                            place.distance
                        )
                    )
                }
                saveList.toList()
            },
            restore = { places ->
                val placesList = ArrayList<Place>()
                places.forEach { list ->
                    val newPlace = Place(
                        id = list[0] as String?,
                        name = list[1] as String?,
                        category = list[2] as PlaceCategory?,
                        address = list[3] as String?,
                        latitude = list[4] as Double?,
                        longitude = list[5] as Double?,
                        adminId = list[6] as String?,
                        description = list[7] as String?,
                        isFavorite = list[8] as Boolean,
                        distance = list[9] as Float?
                    )
                    placesList.add(newPlace)
                }
                placesList.toMutableStateList()
            }
        )
    ) {
        elements.toList().toMutableStateList()
    }
}

@Composable
fun rememberMutableStateListOfPlaceRequest(vararg elements: PlaceRequest): SnapshotStateList<PlaceRequest> {
    return rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                val saveList = ArrayList<List<Any?>>()
                stateList.forEach { placeRequest ->
                    saveList.add(
                        listOf(
                            placeRequest.id,
                            placeRequest.name,
                            placeRequest.category,
                            placeRequest.adminRole,
                            placeRequest.reason,
                            placeRequest.address,
                            placeRequest.latitude,
                            placeRequest.longitude,
                            placeRequest.adminId,
                        )
                    )
                }
                saveList.toList()
            },
            restore = { places ->
                val requestsList = ArrayList<PlaceRequest>()
                places.forEach { list ->
                    val newPlaceRequest = PlaceRequest(
                        id = list[0] as String?,
                        name = list[1] as String?,
                        category = list[2] as PlaceCategory?,
                        adminRole = list[3] as String?,
                        reason = list[4] as String?,
                        address = list[5] as String?,
                        latitude = list[6] as Double?,
                        longitude = list[7] as Double?,
                        adminId = list[8] as String?,
                    )
                    requestsList.add(newPlaceRequest)
                }
                requestsList.toMutableStateList()
            }
        )
    ) {
        elements.toList().toMutableStateList()
    }
}

@Composable
fun rememberMutableStateListOfYourPlace(vararg elements: Any): SnapshotStateList<Any> {
    return rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                val saveList = ArrayList<List<Any?>>()
                stateList.forEach { place ->
                    when (place) {
                        is Place -> saveList.add(
                            listOf(
                                place.id,
                                place.name,
                                place.category,
                                place.address,
                                place.latitude,
                                place.longitude,
                                place.adminId,
                                place.description,
                                place.isFavorite,
                                place.distance
                            )
                        )

                        is PlaceRequest -> saveList.add(
                            listOf(
                                place.id,
                                place.name,
                                place.category,
                                place.adminRole,
                                place.reason,
                                place.address,
                                place.latitude,
                                place.longitude,
                                place.adminId,
                            )
                        )

                        is PlaceDeclined -> saveList.add(
                            listOf(
                                place.id,
                                place.name,
                                place.category,
                                place.address,
                                place.latitude,
                                place.longitude,
                                place.adminId,
                                place.reasonForDeclining,
                            )
                        )
                    }
                }
                saveList.toList()
            },
            restore = { places ->
                val placesList = ArrayList<Any>()
                places.forEach { list ->
                    when (list.size) {
                        10 -> {
                            val newPlace = Place(
                                id = list[0] as String?,
                                name = list[1] as String?,
                                category = list[2] as PlaceCategory?,
                                address = list[3] as String?,
                                latitude = list[4] as Double?,
                                longitude = list[5] as Double?,
                                adminId = list[6] as String?,
                                description = list[7] as String?,
                                isFavorite = list[8] as Boolean,
                                distance = list[9] as Float?
                            )
                            placesList.add(newPlace)
                        }

                        9 -> {
                            val newPlace = PlaceRequest(
                                id = list[0] as String?,
                                name = list[1] as String?,
                                category = list[2] as PlaceCategory?,
                                adminRole = list[3] as String?,
                                reason = list[4] as String?,
                                address = list[5] as String?,
                                latitude = list[6] as Double?,
                                longitude = list[7] as Double?,
                                adminId = list[8] as String?,
                            )
                            placesList.add(newPlace)
                        }

                        8 -> {
                            val newPlace = PlaceDeclined(
                                id = list[0] as String?,
                                name = list[1] as String?,
                                category = list[2] as PlaceCategory?,
                                address = list[3] as String?,
                                latitude = list[4] as Double?,
                                longitude = list[5] as Double?,
                                adminId = list[6] as String?,
                                reasonForDeclining = list[7] as String?,
                            )
                            placesList.add(newPlace)
                        }
                    }
                }
                placesList.toMutableStateList()
            }
        )
    ) {
        elements.toList().toMutableStateList()
    }
}