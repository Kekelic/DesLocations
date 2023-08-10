package com.example.deslocations.presentation.screens.locations_list

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
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
import com.example.deslocations.presentation.components.GrantLocationPermissionContent
import com.example.deslocations.presentation.screens.locations_list.components.LocationsListContent
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsListScreen(
    viewModel: LocationsListViewModel = hiltViewModel(),
    navigateToPlaceDetailsScreen: (String) -> Unit
) {
    val context = LocalContext.current

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val permissionToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var showEnableGPSButton by rememberSaveable {
        mutableStateOf(false)
    }

    var showGrantLocationPermissionContent by rememberSaveable {
        mutableStateOf(false)
    }

    var isInitialGetPlaceDone by rememberSaveable {
        mutableStateOf(false)
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

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {

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

    if (showGrantLocationPermissionContent){
        GrantLocationPermissionContent()
    } else if (showEnableGPSButton) {
        Scaffold {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
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
    } else {
        LocationsListContent(
            navigateToPlaceDetailsScreen = navigateToPlaceDetailsScreen,
            searchPlaces = { searchText -> viewModel.searchPlaces(searchText) },
            refreshPlaces = { viewModel.refreshPlaces() },
            placesResponse = viewModel.placesResponse,
        )
    }


}

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    saveLocation: (Location) -> Unit
) {
    fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                CancellationTokenSource().token

            override fun isCancellationRequested() = false
        }).addOnSuccessListener { location: Location? ->
        if (location == null) {
            showMessage(context, context.resources.getString(R.string.cannot_get_location))
        } else {
            saveLocation(location)
        }

    }
}

fun tryGetLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    showTurnOnLocationDialog: (IntentSenderRequest) -> Unit,
    clearEnableGpsButton: () -> Unit,
    saveLocation: (Location) -> Unit
) {
    val locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()

    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val gpsSettingTask: Task<LocationSettingsResponse> =
        client.checkLocationSettings(builder.build())

    gpsSettingTask.addOnSuccessListener {
        clearEnableGpsButton()
        getCurrentLocation(
            context = context,
            fusedLocationClient = fusedLocationClient,
            saveLocation = saveLocation
        )
    }

    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest =
                    IntentSenderRequest.Builder(exception.resolution).build()
                showTurnOnLocationDialog(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                Log.e(Constants.ERROR_TAG, sendEx.toString())
            }
        }
    }
}

