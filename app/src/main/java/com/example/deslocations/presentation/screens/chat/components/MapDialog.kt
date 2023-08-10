package com.example.deslocations.presentation.screens.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.deslocations.R
import com.example.deslocations.model.Category
import com.example.deslocations.model.PlaceCategory
import com.example.deslocations.presentation.screens.map.components.CustomInfoWindow
import com.example.deslocations.presentation.screens.map.components.bitmapDescriptorFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@Composable
fun MapDialog(
    onDismiss: () -> Unit,
    latLng: LatLng,
    placeName: String,
    category: PlaceCategory,
    categoryName: String,
    address: String,
    distance: Float?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val europe = LatLng(40.0, 22.0)

    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(europe, 1f)
    }
    val markerState = rememberMarkerState(position = latLng)

    var isInitAnimationDone by rememberSaveable {
        mutableStateOf(false)
    }

    val uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    val properties: MapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isBuildingEnabled = true,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            )
        )
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = placeName,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.go_back),
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                    }
                }


                GoogleMap(
                    modifier = Modifier.fillMaxWidth(),
                    properties = properties,
                    cameraPositionState = cameraPositionState,
                    uiSettings = uiSettings,
                    onMapLoaded = {
                        coroutineScope.launch {
                            if (!isInitAnimationDone) {
                                cameraPositionState.animate(
                                    update = CameraUpdateFactory.newCameraPosition(
                                        CameraPosition(
                                            latLng,
                                            15f,
                                            0f,
                                            0f
                                        )
                                    ),
                                    durationMs = 800
                                )
                                isInitAnimationDone = true
                            }
                        }

                    }
                ) {
                    MarkerInfoWindow(
                        state = markerState,
                        icon = bitmapDescriptorFactory(context, Category(category).iconResourceId),
                        onInfoWindowClick = {
                            it.hideInfoWindow()
                        },
                        content = {
                            CustomInfoWindow(
                                title = placeName,
                                category = category,
                                categoryName = categoryName,
                                address = address,
                                distance = distance
                            )
                        }
                    )
                    LaunchedEffect(key1 = Unit) {
                        markerState.showInfoWindow()
                    }
                }

            }


        }
    }


}