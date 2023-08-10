package com.example.deslocations.presentation.screens.make_post.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils.Companion.showMessage
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.presentation.screens.make_post.MakePostViewModel

@Composable
fun AddImagesToDatabase(
    viewModel: MakePostViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    when (val addImageToDatabaseResponse = viewModel.addImageToDatabaseResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> addImageToDatabaseResponse.apply {
            LaunchedEffect(key1 = addImageToDatabaseResponse) {
                if (data != null) {
                    viewModel.currentPost?.let { post ->
                        viewModel.getPlaceName(post.placeID!!)
                        showMessage(
                            context,
                            context.resources.getString(R.string.post_was_created_successfully)
                        )
                    }
                }
            }
        }

        is Response.Failure -> addImageToDatabaseResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }
}