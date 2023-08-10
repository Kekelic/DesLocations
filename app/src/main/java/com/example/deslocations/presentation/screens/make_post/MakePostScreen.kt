package com.example.deslocations.presentation.screens.make_post

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.presentation.screens.make_post.components.AddImagesToDatabase
import com.example.deslocations.presentation.screens.make_post.components.AddPostToDatabase
import com.example.deslocations.presentation.screens.make_post.components.GetPlaceNameCallback
import com.example.deslocations.presentation.screens.make_post.components.MakePostContent

@Composable
fun MakePostScreen(
    placeID: String,
    viewModel: MakePostViewModel = hiltViewModel(),
    navigateToPlaceDetailsScreen: (String) -> Unit
) {
    MakePostContent(
        placeID = placeID,
        makePost = { post -> viewModel.makePost(post) },
    )

    AddPostToDatabase()

    AddImagesToDatabase()

    GetPlaceNameCallback(navigateToPlaceDetailsScreen = navigateToPlaceDetailsScreen)


}