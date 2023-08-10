package com.example.deslocations.presentation.screens.your_locations

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.presentation.screens.your_locations.components.YourLocationsContent

@Composable
fun YourLocationsScreen(
    viewModel: YourLocationsViewModel = hiltViewModel(),
    navigateToPlaceDetailsScreen: (String) -> Unit
) {

    YourLocationsContent(
        deletePlaceDeclined = { place -> viewModel.deletePlaceDeclined(place) },
        cancelPlaceRequest = { place -> viewModel.cancelPlaceRequest(place) },
        navigateToPlaceDetailsScreen = navigateToPlaceDetailsScreen,
        refreshUserPlaces = { viewModel.refreshUserPlaces() },
        placesResponse = viewModel.placesResponse,
        deletingPlaceDeclinedResponse = viewModel.deletingPlaceDeclinedResponse,
        cancelingPlaceRequestResponse = viewModel.cancelingPlaceRequestResponse,
        resetPlacesResponse = { viewModel.resetPlacesResponse() },
        resetDeletingPlaceDeclinedResponse = { viewModel.resetDeletingPlaceDeclinedResponse() },
        resetCancelingPlaceRequestResponse = { viewModel.resetCancelingPlaceRequestResponse() }
    )
}