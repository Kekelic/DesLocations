package com.example.deslocations.presentation.screens.make_post.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils
import com.example.deslocations.model.NotificationData
import com.example.deslocations.model.PushNotification
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.presentation.screens.make_post.MakePostViewModel

@Composable
fun GetPlaceNameCallback(
    viewModel: MakePostViewModel = hiltViewModel(),
    navigateToPlaceDetailsScreen: (String) -> Unit
) {
    val context = LocalContext.current

    when (val getPlaceNameResponse = viewModel.getPlaceNameResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> getPlaceNameResponse.apply {
            LaunchedEffect(key1 = getPlaceNameResponse) {
                if (data != null) {
                    viewModel.currentPost?.let { post ->
                        viewModel.sendNotification(
                            PushNotification(
                                NotificationData(
                                    title = data,
                                    text = post.title!!,
                                    postID = post.id!!
                                ),
                                to = "${Constants.TOPIC}${post.placeID}"
                            )
                        )
                        navigateToPlaceDetailsScreen(post.placeID!!)
                    }

                }
            }
        }

        is Response.Failure -> getPlaceNameResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                Utils.showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }
}