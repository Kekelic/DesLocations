package com.example.deslocations.presentation.screens.map.components

import android.location.Location
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.FilterAlt
import androidx.compose.material.icons.twotone.Map
import androidx.compose.material.icons.twotone.MyLocation
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.deslocations.R
import com.example.deslocations.model.Category
import com.example.deslocations.model.Place
import com.example.deslocations.model.PlaceCategory
import com.example.deslocations.presentation.components.rememberMutableStateListOf
import com.example.deslocations.presentation.components.rememberMutableStateListOfPlace
import com.example.deslocations.ui.theme.AppGray
import com.example.deslocations.ui.theme.AppLightBlack
import com.example.deslocations.ui.theme.AppTransparentGray
import com.example.deslocations.ui.theme.AppTransparentWhite
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapContent(
    userLocation: Location?,
    places: List<Place>,
    navigateToPlaceDetailsScreen: (String) -> Unit,
    refreshPlaces: () -> Unit,
) {
    val context = LocalContext.current

    val europe = LatLng(40.0, 22.0)

    val cameraPositionState = rememberCameraPositionState {
        position = if (userLocation != null) {
            CameraPosition.fromLatLngZoom(
                LatLng(userLocation.latitude, userLocation.longitude),
                10f
            )
        } else {
            CameraPosition.fromLatLngZoom(europe, 1f)
        }
    }

    var isMarkerVisible by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { cameraPositionState.position.zoom }
            .collect { zoom ->
                isMarkerVisible = zoom > 6f
            }
    }

    val coroutineScope = rememberCoroutineScope()

    var isFilterDialogShown by rememberSaveable {
        mutableStateOf(false)
    }


    var isInitSetupDone by rememberSaveable {
        mutableStateOf(false)
    }

    val checkedCategories = rememberMutableStateListOf<PlaceCategory>()


    val filteredPlaces = rememberMutableStateListOfPlace()

    LaunchedEffect(isInitSetupDone) {
        if (!isInitSetupDone) {
            checkedCategories.clear()
            checkedCategories.addAll(PlaceCategory.values())
            filteredPlaces.clear()
            filteredPlaces.addAll(places)
            isInitSetupDone = true
        }
    }


    var isMapTypeDialogShown by rememberSaveable {
        mutableStateOf(false)
    }

    var isInitAnimationDone by rememberSaveable {
        mutableStateOf(false)
    }


    var typeOfMap by rememberSaveable {
        mutableStateOf(MapType.NORMAL)
    }


    val uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    var properties: MapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = typeOfMap,
                isBuildingEnabled = true,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            )
        )
    }



    Scaffold {
        ConstraintLayout(modifier = Modifier.padding(it)) {
            val (options) = createRefs()
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = properties,
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                onMapLoaded = {
                    coroutineScope.launch {
                        if (userLocation != null && !isInitAnimationDone) {
                            isInitAnimationDone = true
                            animateToMyLocation(cameraPositionState, userLocation)
                        }
                    }
                }
            ) {
                MapMaker(
                    position = LatLng(
                        userLocation?.latitude ?: 45.0,
                        userLocation?.longitude ?: 16.0
                    ),
                    iconResourceId = R.drawable.person,
                    onInfoWindowClick = {},
                    visible = true,
                    with = 78,
                    height = 90,
                ) {
                    Text(text = stringResource(id = R.string.you))
                }
                filteredPlaces.forEach { place ->
                    MapMaker(
                        position = LatLng(place.latitude!!, place.longitude!!),
                        iconResourceId = Category(place.category).iconResourceId,
                        onInfoWindowClick = {
                            coroutineScope.launch {
                                navigateToPlaceDetailsScreen(place.id!!)
                            }
                        },
                        title = place.name!!,
                        titleColor = if (typeOfMap == MapType.HYBRID) Color.White else Color.Black,
                        titleBorder = if (typeOfMap == MapType.HYBRID) AppGray else Color.White,
                        visible = isMarkerVisible,
                    ) {
                        CustomInfoWindow(
                            title = place.name ?: "",
                            category = place.category ?: PlaceCategory.OTHER,
                            categoryName = place.categoryName ?: "",
                            address = place.address ?: "",
                            distance = place.distance ?: 0f
                        )
                    }

                }


            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(options) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(5.dp)
                .clip(shape = RoundedCornerShape(3.dp))
                .background(AppTransparentWhite)
                .border(
                    border = BorderStroke(1.dp, AppTransparentGray),
                    shape = RoundedCornerShape(3.dp)
                ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(end = 5.dp, bottom = 5.dp),
                    onClick = {
                        isFilterDialogShown = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.FilterAlt,
                        contentDescription = stringResource(id = R.string.filter),
                        tint = AppLightBlack
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(end = 5.dp, bottom = 5.dp),
                    onClick = {
                        refreshPlaces()
                        isInitSetupDone = false
                    },
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Refresh,
                        contentDescription = stringResource(id = R.string.refresh),
                        tint = AppLightBlack
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(end = 5.dp, bottom = 5.dp),
                    onClick = {
                        coroutineScope.launch {
                            if (userLocation != null) {
                                animateToMyLocation(cameraPositionState, userLocation)
                            }
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.MyLocation,
                        contentDescription = stringResource(id = R.string.my_locations),
                        tint = AppLightBlack
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(end = 5.dp, bottom = 5.dp),
                    onClick = {
                        isMapTypeDialogShown = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Map,
                        contentDescription = stringResource(id = R.string.map_style),
                        tint = AppLightBlack
                    )
                }


            }
        }
    }


    if (isFilterDialogShown) {
        FilterDialog(
            onDismiss = { isFilterDialogShown = false },
            listCheckedCategories = checkedCategories,
            addCategory = { placeCategory -> checkedCategories.add(placeCategory) },
            removeCategory = { placeCategory -> checkedCategories.remove(placeCategory) },
            filterPlaces = {
                filteredPlaces.clear()
                filteredPlaces.addAll(filterPlaces(places, checkedCategories))
            }
        )
    }

    if (isMapTypeDialogShown) {
        MapTypeDialog(
            onDismiss = { isMapTypeDialogShown = false },
            typeOfMap = typeOfMap,
            changeMapType = { mapType ->
                typeOfMap = mapType
                properties = MapProperties(
                    mapType = typeOfMap,
                    isBuildingEnabled = true,
                    mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                        context,
                        R.raw.map_style
                    )
                )
            }
        )
    }

}

private fun filterPlaces(
    places: List<Place>,
    checkedCategories: List<PlaceCategory>
): List<Place> {
    val filteredPlaces = ArrayList<Place>()
    places.forEach { place ->
        checkedCategories.forEach { placeCategory ->
            if (place.category == placeCategory) {
                filteredPlaces.add(place)
            }
        }
    }
    return filteredPlaces
}

private suspend fun animateToMyLocation(
    cameraPositionState: CameraPositionState,
    userLocation: Location
) {
    cameraPositionState.animate(
        update = CameraUpdateFactory.newCameraPosition(
            CameraPosition(
                LatLng(userLocation.latitude, userLocation.longitude),
                15f,
                0f,
                0f
            )
        ),
        durationMs = 1000
    )
}
