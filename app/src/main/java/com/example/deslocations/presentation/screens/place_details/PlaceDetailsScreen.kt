package com.example.deslocations.presentation.screens.place_details

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils.Companion.showMessage
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.presentation.screens.place_details.components.PlaceDetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsScreen(
    placeID: String,
    viewModel: PlaceDetailsViewModel = hiltViewModel(),
    navigateToMakePost: (String) -> Unit,
    navigateToPostDetailsScreen: (String) -> Unit,
    navigateToChatScreen: (String) -> Unit
) {
    val context = LocalContext.current

    var isInitialGetPlaceDone by rememberSaveable {
        mutableStateOf(false)
    }

    var isInitialGetPostsDone by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        if (!isInitialGetPlaceDone) {
            viewModel.getPlace(placeID)
            isInitialGetPlaceDone = true
        }
    }

    when (val getPlaceDetailsResponse = viewModel.getPlaceDetailsResponse) {
        is Response.Loading -> {
            Scaffold {
                Box(modifier = Modifier.padding(it), contentAlignment = Alignment.Center) {
                    ProgressBar()
                }
            }
        }

        is Response.Success -> getPlaceDetailsResponse.apply {
            LaunchedEffect(key1 = getPlaceDetailsResponse) {
                if (!isInitialGetPostsDone) {
                    viewModel.getPosts(data!!)
                    isInitialGetPostsDone = true
                }
            }
            PlaceDetailsContent(
                place = data!!,
                navigateToMakePost = navigateToMakePost,
                navigateToPostDetailsScreen = navigateToPostDetailsScreen,
                navigateToChatScreen = navigateToChatScreen,
                deleteFavorite = { placeID -> viewModel.deleteFavorite(placeID) },
                makeFavorite = { placeID -> viewModel.makeFavorite(placeID) },
                updatePlaceDescription = { placeID, description ->
                    viewModel.updatePlaceDescription(
                        placeID,
                        description
                    )
                },
                isAdmin = viewModel.currentUser?.let { it.uid == data.adminId } ?: false,
                refreshPosts = { place -> viewModel.refreshPosts(place) },
                refreshPostResponse = viewModel.refreshPostsResponse,
                getPostsResponse = viewModel.getPostsResponse,
                getPosts = { place -> viewModel.getPosts(place) }
            )
        }

        is Response.Failure -> getPlaceDetailsResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
            Scaffold {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        viewModel.getPlace(placeID)
                    }) {
                        Text(text = stringResource(id = R.string.refresh))
                    }
                }
            }
        }
    }
}