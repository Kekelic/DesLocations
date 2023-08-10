package com.example.deslocations.presentation.screens.make_post.components

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
import com.example.deslocations.presentation.screens.make_post.MakePostViewModel

@Composable
fun AddPostToDatabase(
    viewModel: MakePostViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    when (val addPostToDatabaseResponse = viewModel.addPostToDatabaseResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> addPostToDatabaseResponse.apply {
            LaunchedEffect(key1 = addPostToDatabaseResponse) {
                if (data != null) {
                    viewModel.currentPost = data
                    viewModel.addImageToDatabase(data)
                }
            }
        }

        is Response.Failure -> addPostToDatabaseResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                Utils.showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }
}