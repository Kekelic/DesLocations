package com.example.deslocations.presentation.screens.post_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.deslocations.R
import com.example.deslocations.model.PostType
import com.example.deslocations.model.response.PostResponse
import com.example.deslocations.ui.theme.AppBlue
import com.example.deslocations.ui.theme.AppGreen
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsContent(
    post: PostResponse?,
    canDelete: (String) -> Boolean,
    deletePost: (PostResponse) -> Unit,
    isAdminPost: (String) -> Boolean
) {
    var isDeletePostDialogShown by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold { paddingValues ->
        if (post == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.post_is_deleted))
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            post.title?.let {
                                Text(
                                    text = it,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            post.authorID?.let { authorID ->
                                if (canDelete(authorID)) {
                                    OutlinedIconButton(
                                        onClick = { isDeletePostDialogShown = true },
                                        shape = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = stringResource(id = R.string.delete_post),
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        post.type?.let {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 15.dp),
                            ) {
                                Text(
                                    text = stringResource(id = R.string.type) + ": $it",
                                )
                                Icon(
                                    imageVector = if (post.type == PostType.EVENT) Icons.Default.Event
                                    else Icons.Default.Info,
                                    contentDescription = if (post.type == PostType.EVENT) stringResource(
                                        id = R.string.event
                                    )
                                    else stringResource(id = R.string.information),
                                    tint = if (post.type == PostType.EVENT) AppGreen
                                    else AppBlue
                                )
                            }

                        }

                        if (post.type == PostType.EVENT) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = stringResource(id = R.string.start_date) + ": ${post.startDate}",
                                modifier = Modifier.padding(horizontal = 15.dp),
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        post.description?.let {
                            Text(text = it, modifier = Modifier.padding(horizontal = 15.dp))
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        post.authorName?.let {
                            Text(
                                text = stringResource(id = R.string.author) + ": $it",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                fontWeight = if (isAdminPost(post.authorID!!)) FontWeight.W500 else FontWeight.W300,
                                textAlign = TextAlign.End
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = SimpleDateFormat(
                                "dd/MM/yyyy 'at' HH:mm",
                                Locale.getDefault()
                            ).format(post.date!!),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }
                itemsIndexed(items = post.imageUris!!) { _, uri ->
                    Box(
                        modifier = Modifier
                            .animateItemPlacement()
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.width(280.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = stringResource(id = R.string.post_image),
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                    }
                }
            }
        }
    }

    if (isDeletePostDialogShown) {
        post?.let {
            DeletePostDialog(
                onDismiss = { isDeletePostDialogShown = false },
                postTitle = post.title!!,
                deletePost = { deletePost(post) }
            )
        }
    }

}
