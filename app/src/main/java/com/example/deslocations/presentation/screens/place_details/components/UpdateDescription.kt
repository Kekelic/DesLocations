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
fun UpdateDescription(
    viewModel: PlaceDetailsViewModel = hiltViewModel(),
    setToUpdated: () -> Unit
) {
    val context = LocalContext.current

    when (val updateDescriptionResponse = viewModel.updateDescriptionResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> updateDescriptionResponse.apply {
            LaunchedEffect(key1 = updateDescriptionResponse) {
                if (data != "") {
                    setToUpdated()
                }
            }

        }

        is Response.Failure -> updateDescriptionResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                Utils.showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }
}