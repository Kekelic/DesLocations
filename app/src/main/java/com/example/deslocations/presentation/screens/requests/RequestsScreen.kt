package com.example.deslocations.presentation.screens.requests

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.presentation.screens.requests.components.RequestsContent

@Composable
fun RequestsScreen(
    viewModel: RequestsViewModel = hiltViewModel()
) {

    RequestsContent(
        makePlace = { placeRequest ->
            viewModel.approvePlaceRequest(placeRequest)
        },
        declinePlaceRequest = { placeRequest, reasonForDecliningRequest ->
            viewModel.declinePlaceRequest(placeRequest, reasonForDecliningRequest)
        },
        approveRequestResponse = viewModel.approveRequestResponse,
        declinedRequestResponse = viewModel.declineRequestResponse,
        requestsResponse = viewModel.requestsResponse,
        refreshPlaceRequests = { viewModel.refreshPlaceRequests() },
        resetRequestsResponse = { viewModel.resetRequestsResponse() },
        resetApproveRequestResponse = { viewModel.resetApproveRequestResponse() },
        resetDeclineRequestResponse = { viewModel.resetDeclineRequestResponse() }
    )


}