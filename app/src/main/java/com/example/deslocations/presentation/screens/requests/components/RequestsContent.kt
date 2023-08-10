@file:OptIn(ExperimentalMaterialApi::class)

package com.example.deslocations.presentation.screens.requests.components

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
import com.example.deslocations.model.PlaceRequest
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.presentation.components.rememberMutableStateListOfPlaceRequest


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RequestsContent(
    makePlace: (PlaceRequest) -> Unit,
    declinePlaceRequest: (PlaceRequest, String) -> Unit,
    approveRequestResponse: Response<PlaceRequest>,
    declinedRequestResponse: Response<PlaceRequest>,
    requestsResponse: Response<List<PlaceRequest>>,
    refreshPlaceRequests: () -> Unit,
    resetRequestsResponse: () -> Unit,
    resetApproveRequestResponse: () -> Unit,
    resetDeclineRequestResponse: () -> Unit,
) {
    val context = LocalContext.current

    val requests = rememberMutableStateListOfPlaceRequest()

    var isInitGetRequestsDone by rememberSaveable {
        mutableStateOf(false)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = requestsResponse is Response.Loading,
        onRefresh = { refreshPlaceRequests() })


    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(paddingValues)
        ) {
            if (requests.isEmpty() && isInitGetRequestsDone) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(id = R.string.no_request))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(items = requests, key = { it.id!! }) { placeRequest ->
                        RequestModeratorItem(
                            modifier = Modifier.animateItemPlacement(),
                            placeRequest = placeRequest,
                            makePlace = { requestPlace ->
                                makePlace(requestPlace)
                            },
                            declinePlaceRequest = { requestPlace, reasonForDecliningRequest ->
                                declinePlaceRequest(requestPlace, reasonForDecliningRequest)
                            }
                        )
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = requestsResponse is Response.Loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }

    when (requestsResponse) {
        is Response.Loading -> {}
        is Response.Success -> requestsResponse.apply {
            LaunchedEffect(key1 = requestsResponse) {
                if (data != null) {
                    requests.clear()
                    requests.addAll(data)
                    isInitGetRequestsDone = true
                    resetRequestsResponse()
                }
            }
        }

        is Response.Failure -> requestsResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }

    when (approveRequestResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> approveRequestResponse.apply {
            LaunchedEffect(key1 = approveRequestResponse) {
                if (data != null) {
                    showMessage(
                        context,
                        context.resources.getString(R.string.successfully_approved_request)
                    )
                    requests.remove(data)
                    resetApproveRequestResponse()
                }
            }
        }

        is Response.Failure -> approveRequestResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }

    when (declinedRequestResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> declinedRequestResponse.apply {
            LaunchedEffect(data) {
                if (data != null) {
                    showMessage(
                        context,
                        context.resources.getString(R.string.request_successfully_declined)
                    )
                    requests.remove(data)
                    resetDeclineRequestResponse()
                }
            }
        }

        is Response.Failure -> declinedRequestResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }

}