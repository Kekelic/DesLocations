package com.example.deslocations.presentation.screens.make_request

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils.Companion.showMessage
import com.example.deslocations.model.PlaceRequest
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.presentation.screens.make_request.components.MakeRequestContent
import com.google.android.gms.location.LocationServices

@Composable
fun MakeRequestScreen(
    viewModel: MakeRequestViewModel = hiltViewModel(),
    navigateToMapScreen: () -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    viewModel.userLocation = location
                }
        }
    }

    MakeRequestContent(
        makeRequest = { placeRequest: PlaceRequest ->
            viewModel.makeRequest(placeRequest)
        },
        userLocation = viewModel.userLocation,
        currentUser = viewModel.currentUser
    )



    when (val makeRequestResponse = viewModel.makeRequestResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> LaunchedEffect(Unit) {
            if (makeRequestResponse.data!!) {
                navigateToMapScreen()
                showMessage(
                    context,
                    context.resources.getString(R.string.successfully_made_request)
                )
            }

        }

        is Response.Failure -> makeRequestResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }

}