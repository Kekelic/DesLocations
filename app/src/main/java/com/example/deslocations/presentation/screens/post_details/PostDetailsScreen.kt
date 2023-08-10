package com.example.deslocations.presentation.screens.post_details

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
import com.example.deslocations.presentation.screens.post_details.components.PostDetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    viewModel: PostDetailsViewModel = hiltViewModel(),
    postID: String,
    navigateToPlaceDetailsScreen: (String) -> Unit
) {
    val context = LocalContext.current
    var isInitGetPostDone by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = isInitGetPostDone) {
        if (!isInitGetPostDone) {
            viewModel.getPost(postID)
            isInitGetPostDone = true
        }
    }

    when (val getPostResponse = viewModel.getPostResponse) {
        is Response.Loading -> {
            LaunchedEffect(Unit) {
                isInitGetPostDone = false
            }
            Scaffold {
                Box(modifier = Modifier.padding(it), contentAlignment = Alignment.Center) {
                    ProgressBar()
                }
            }
        }
        is Response.Success -> getPostResponse.apply {
            LaunchedEffect(true) {
                data?.let {
                    viewModel.getAdminState(data.placeID!!)
                }
            }
            PostDetailsContent(
                post = data,
                canDelete = { authorID ->
                    viewModel.currentUser?.let {
                        it.uid == authorID || it.uid == viewModel.adminID
                    } ?: false
                },
                deletePost = { post -> viewModel.deletePost(post) },
                isAdminPost = { authorID -> authorID == viewModel.adminID }
            )
        }

        is Response.Failure -> getPostResponse.apply {
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
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(onClick = {
                            viewModel.getPost(postID)
                        }) {
                            Text(text = stringResource(id = R.string.refresh))
                        }
                    }
                }
            }
        }
    }

    when (val deletePostResponse = viewModel.deletePostResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> deletePostResponse.apply {
            LaunchedEffect(key1 = deletePostResponse) {
                if (data != null) {
                    navigateToPlaceDetailsScreen(data)
                }
            }
        }

        is Response.Failure -> deletePostResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }


}