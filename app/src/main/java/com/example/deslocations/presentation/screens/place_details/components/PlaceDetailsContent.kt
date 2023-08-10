@file:OptIn(ExperimentalMaterialApi::class)

package com.example.deslocations.presentation.screens.place_details.components

import android.Manifest
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.rounded.Comment
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils.Companion.showMessage
import com.example.deslocations.model.PlaceCategory
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.PlaceResponse
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.ui.theme.AppGreen
import com.example.deslocations.ui.theme.AppRed
import com.example.deslocations.ui.theme.AppYellow

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun PlaceDetailsContent(
    place: PlaceResponse,
    navigateToMakePost: (String) -> Unit,
    navigateToPostDetailsScreen: (String) -> Unit,
    navigateToChatScreen: (String) -> Unit,
    deleteFavorite: (String) -> Unit,
    makeFavorite: (String) -> Unit,
    updatePlaceDescription: (String, String) -> Unit,
    isAdmin: Boolean,
    refreshPosts: (PlaceResponse) -> Unit,
    refreshPostResponse: Response<PlaceResponse>,
    getPostsResponse: Response<PlaceResponse>,
    getPosts: (PlaceResponse) -> Unit,

    ) {
    val context = LocalContext.current
    var description by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(place.description!!))
    }

    val focusManager = LocalFocusManager.current

    var isUpdated by rememberSaveable { mutableStateOf(true) }

    var isFavorite by rememberSaveable { mutableStateOf(place.isFavorite) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshPostResponse is Response.Loading,
        onRefresh = { refreshPosts(place) })


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                makeFavorite(place.id!!)
            } else {
                showMessage(
                    context,
                    context.resources.getString(R.string.notification_permission_denied)
                )
            }
        }
    )

    ConstraintLayout {
        val (chat) = createRefs()
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(start = 10.dp, end = 10.dp)
                    .pullRefresh(pullRefreshState),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn {
                    item {
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = place.name!!,
                                modifier = Modifier
                                    .weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                            IconButton(
                                onClick = {
                                    if (isFavorite) {
                                        deleteFavorite(place.id!!)
                                    } else {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        } else makeFavorite(place.id!!)
                                    }
                                },
                            ) {
                                val icon =
                                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                                val color =
                                    if (isFavorite) AppRed else MaterialTheme.colorScheme.primary
                                Icon(
                                    imageVector = icon,
                                    contentDescription = stringResource(id = R.string.make_favorite),
                                    tint = color
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        place.category?.let {
                            Text(
                                text = if (it == PlaceCategory.OTHER) stringResource(id = R.string.category) + ": ${
                                    it.getLabel(
                                        context
                                    )
                                }, ${place.categoryName}"
                                else stringResource(id = R.string.category) + ": ${
                                    it.getLabel(
                                        context
                                    )
                                }",

                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }

                        if (isAdmin) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = description,
                                onValueChange = {
                                    isUpdated = false
                                    description = it
                                },
                                label = {
                                    Text(text = stringResource(id = R.string.place_description))
                                },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                trailingIcon = {
                                    val color = if (isUpdated) AppGreen else AppYellow
                                    IconButton(
                                        onClick = {
                                            if (isUpdated) showMessage(
                                                context,
                                                context.resources.getString(R.string.already_updated)
                                            )
                                            else updatePlaceDescription(
                                                place.id!!,
                                                description.text
                                            )
                                            focusManager.clearFocus()
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = stringResource(id = R.string.update_description),
                                            tint = color
                                        )
                                    }
                                }
                            )
                        } else if (!TextUtils.isEmpty(place.description!!.trim())) {
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = place.description!!,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        FilledTonalButton(
                            onClick = { navigateToMakePost(place.id!!) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(id = R.string.make_post),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(
                                imageVector = Icons.Default.PostAdd,
                                contentDescription = stringResource(id = R.string.make_post),
                                modifier = Modifier.padding(bottom = 4.dp),
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    when (getPostsResponse) {
                        is Response.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp), contentAlignment = Alignment.Center
                                ) {
                                    ProgressBar()
                                }
                            }
                        }

                        is Response.Success -> getPostsResponse.apply {
                            if (place.posts.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = stringResource(id = R.string.no_posts))
                                    }
                                }
                            } else {
                                itemsIndexed(items = place.posts) { _, post ->
                                    PostItem(
                                        modifier = Modifier
                                            .animateItemPlacement()
                                            .clickable { navigateToPostDetailsScreen(post.id!!) },
                                        post = post,
                                        isAdminPost = place.adminId == post.authorID,
                                    )
                                }
                            }
                        }

                        is Response.Failure -> getPostsResponse.apply {
                            item {
                                LaunchedEffect(e) {
                                    Log.e(Constants.ERROR_TAG, e.message.toString())
                                    showMessage(
                                        context,
                                        context.getString(R.string.error_something_went_wrong)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp), contentAlignment = Alignment.Center
                                ) {
                                    Button(onClick = {
                                        getPosts(place)
                                    }) {
                                        Text(text = stringResource(id = R.string.refresh))
                                    }
                                }
                            }
                        }
                    }


                }
                if (getPostsResponse is Response.Success) {
                    PullRefreshIndicator(
                        refreshing = refreshPostResponse is Response.Loading,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .constrainAs(chat) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 30.dp, end = 5.dp)
                .size(44.dp),
            onClick = {
                navigateToChatScreen(place.id!!)
            },
            shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 15.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Comment,
                contentDescription = stringResource(id = R.string.open_chat),
                modifier = Modifier.size(30.dp)
            )
        }
    }

    when (refreshPostResponse) {
        is Response.Loading -> {}
        is Response.Success -> {}
        is Response.Failure -> refreshPostResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }

    UpdateDescription(setToUpdated = { isUpdated = true })

    ChangeFavoriteState(changeFavoriteState = {
        isFavorite = !isFavorite
    })

}
