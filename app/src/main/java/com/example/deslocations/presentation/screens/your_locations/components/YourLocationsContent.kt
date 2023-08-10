@file:OptIn(ExperimentalMaterialApi::class)

package com.example.deslocations.presentation.screens.your_locations.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils.Companion.showMessage
import com.example.deslocations.model.Place
import com.example.deslocations.model.PlaceDeclined
import com.example.deslocations.model.PlaceRequest
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.presentation.components.rememberMutableStateListOfYourPlace
import com.example.deslocations.presentation.screens.locations_list.components.PlaceItem

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun YourLocationsContent(
    deletePlaceDeclined: (PlaceDeclined) -> Unit,
    cancelPlaceRequest: (PlaceRequest) -> Unit,
    navigateToPlaceDetailsScreen: (String) -> Unit,
    refreshUserPlaces: () -> Unit,
    placesResponse: Response<List<Any>>,
    deletingPlaceDeclinedResponse: Response<PlaceDeclined>,
    cancelingPlaceRequestResponse: Response<PlaceRequest>,
    resetPlacesResponse: () -> Unit,
    resetDeletingPlaceDeclinedResponse: () -> Unit,
    resetCancelingPlaceRequestResponse: () -> Unit,
) {
    val context = LocalContext.current

    val places = rememberMutableStateListOfYourPlace()

    var isInitGetPlacesDone by rememberSaveable {
        mutableStateOf(false)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = placesResponse is Response.Loading,
        onRefresh = { refreshUserPlaces() })

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(it)
        ) {
            if (places.isEmpty() && isInitGetPlacesDone) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(id = R.string.you_dont_have_any_place))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(items = places, key = { place ->
                        when (place) {
                            is Place -> place.id
                            is PlaceDeclined -> place.id
                            is PlaceRequest -> place.id
                            else -> ""
                        }!!
                    }) { place ->
                        when (place) {
                            is Place -> PlaceItem(
                                modifier = Modifier
                                    .animateItemPlacement(),
                                place = place,
                                navigateToPlaceDetailsScreen = navigateToPlaceDetailsScreen
                            )

                            is PlaceRequest -> PlaceRequestItem(
                                modifier = Modifier
                                    .animateItemPlacement(),
                                placeRequest = place,
                                cancelPlaceRequest = cancelPlaceRequest
                            )

                            is PlaceDeclined -> PlaceDeclinedItem(
                                modifier = Modifier
                                    .animateItemPlacement(),
                                placeDeclined = place,
                                deletePlaceDeclined = deletePlaceDeclined
                            )
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = placesResponse is Response.Loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }


    when (placesResponse) {
        is Response.Loading -> {}
        is Response.Success -> placesResponse.apply {
            LaunchedEffect(key1 = placesResponse) {
                if (data != null) {
                    places.clear()
                    places.addAll(data)
                    isInitGetPlacesDone = true
                    resetPlacesResponse()
                }
            }
        }

        is Response.Failure -> placesResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }


    when (deletingPlaceDeclinedResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> deletingPlaceDeclinedResponse.apply {
            LaunchedEffect(key1 = deletingPlaceDeclinedResponse) {
                if (data != null) {
                    showMessage(
                        context,
                        context.resources.getString(R.string.successfully_deleted)
                    )
                    places.remove(data)
                    resetDeletingPlaceDeclinedResponse()
                }
            }
        }

        is Response.Failure -> deletingPlaceDeclinedResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }

    when (cancelingPlaceRequestResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> cancelingPlaceRequestResponse.apply {
            LaunchedEffect(key1 = cancelingPlaceRequestResponse) {
                if (data != null) {
                    showMessage(
                        context,
                        context.resources.getString(R.string.place_request_successfully_canceled)
                    )
                    places.remove(data)
                    resetCancelingPlaceRequestResponse()
                }
            }
        }

        is Response.Failure -> cancelingPlaceRequestResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }
}