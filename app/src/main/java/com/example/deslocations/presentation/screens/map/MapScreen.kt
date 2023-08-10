package com.example.deslocations.presentation.screens.map

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils.Companion.showMessage
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.GrantLocationPermissionContent
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.presentation.screens.locations_list.getCurrentLocation
import com.example.deslocations.presentation.screens.locations_list.tryGetLocation
import com.example.deslocations.presentation.screens.map.components.MapContent
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    navigateToPlaceDetailsScreen: (String) -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val permissionToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var showEnableGPSButton by rememberSaveable {
        mutableStateOf(false)
    }

    var isInitialGetPlaceDone by rememberSaveable {
        mutableStateOf(false)
    }

    var showGrantLocationPermissionContent by rememberSaveable {
        mutableStateOf(false)
    }

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            showEnableGPSButton = false
            getCurrentLocation(context = context,
                fusedLocationClient = fusedLocationClient,
                saveLocation = { location -> viewModel.saveLocation(location) }
            )
        } else {
            showEnableGPSButton = true
        }

    }

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showGrantLocationPermissionContent = false
                tryGetLocation(
                    context = context,
                    fusedLocationClient = fusedLocationClient,
                    showTurnOnLocationDialog = { intentSenderRequest ->
                        settingResultRequest.launch(
                            intentSenderRequest
                        )
                    },
                    clearEnableGpsButton = { showEnableGPSButton = false },
                    saveLocation = { location -> viewModel.saveLocation(location) }
                )

            } else {
                showMessage(context, context.resources.getString(R.string.permission_denied))
                showGrantLocationPermissionContent = true
            }

        })


    LaunchedEffect(Unit) {
        multiplePermissionResultLauncher.launch(permissionToRequest)
    }

    if (viewModel.userLocation != null) {
        LaunchedEffect(Unit) {
            if (!isInitialGetPlaceDone) {
                viewModel.getPlaces()
                isInitialGetPlaceDone = true
            }
        }
    } else {
        isInitialGetPlaceDone = false
    }


    Scaffold {
        Box(modifier = Modifier.padding(it)) {}
    }

    if (!showGrantLocationPermissionContent) {
        when (val placesResponse = viewModel.placesResponse) {
            is Response.Loading -> ProgressBar()
            is Response.Success -> placesResponse.apply {
                if (data != null) {
                    MapContent(
                        places = data,
                        userLocation = viewModel.userLocation,
                        navigateToPlaceDetailsScreen = navigateToPlaceDetailsScreen,
                        refreshPlaces = { viewModel.refreshPlaces() },
                    )
                }
            }

            is Response.Failure -> placesResponse.apply {
                LaunchedEffect(e) {
                    Log.e(Constants.ERROR_TAG, e.message.toString())
                    showMessage(context, context.getString(R.string.error_something_went_wrong))
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = {
                        viewModel.getPlaces()
                    }) {
                        Text(text = stringResource(id = R.string.refresh))
                    }
                }
            }
        }
    } else {
        GrantLocationPermissionContent()
    }


    if (showEnableGPSButton) {
        Scaffold {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.access_to_location_message),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = {
                        tryGetLocation(
                            context = context,
                            fusedLocationClient = fusedLocationClient,
                            showTurnOnLocationDialog = { intentSenderRequest ->
                                settingResultRequest.launch(
                                    intentSenderRequest
                                )
                            },
                            clearEnableGpsButton = { showEnableGPSButton = false },
                            saveLocation = { location -> viewModel.saveLocation(location) }
                        )
                    }) {
                        Text(text = stringResource(id = R.string.enable_location_service))
                    }
                }

            }
        }

    }

}
