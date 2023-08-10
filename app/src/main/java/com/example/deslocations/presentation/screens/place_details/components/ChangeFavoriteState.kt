package com.example.deslocations.presentation.screens.place_details.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.presentation.screens.place_details.PlaceDetailsViewModel

@Composable
fun ChangeFavoriteState(
    viewModel: PlaceDetailsViewModel = hiltViewModel(),
    changeFavoriteState: () -> Unit
) {
    val context = LocalContext.current

    when (val changeFavoriteStateResponse = viewModel.changeFavoriteStateResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> changeFavoriteStateResponse.apply {
            LaunchedEffect(key1 = changeFavoriteStateResponse) {
                if (data == true) {
                    changeFavoriteState()
                    viewModel.changeFavoriteStateResponse = Response.Success(false)
                }
            }
        }

        is Response.Failure -> changeFavoriteStateResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                Utils.showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }
}