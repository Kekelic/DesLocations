package com.example.deslocations.presentation.screens.place_details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun PostItem(
    modifier: Modifier,
    post: PostResponse,
    isAdminPost: Boolean,
) {
    
    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(200.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            if (post.isImageRight!!) {
                PostDescription(
                    modifier = Modifier.weight(0.6f),
                    post = post,
                    isAdminPost = isAdminPost
                )
                if (post.imageUris != null) {
                    PostImage(
                        modifier = Modifier.weight(0.4f),
                        post = post,
                    )
                }
            } else {
                if (post.imageUris != null) {
                    PostImage(
                        modifier = Modifier.weight(0.4f),
                        post = post,
                    )
                }
                PostDescription(
                    modifier = Modifier.weight(0.6f),
                    post = post,
                    isAdminPost = isAdminPost
                )
            }


        }

    }

}

@Composable
fun PostDescription(
    modifier: Modifier,
    post: PostResponse,
    isAdminPost: Boolean
) {
    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            post.title?.let {
                Icon(
                    imageVector = if (post.type == PostType.EVENT) Icons.Default.Event
                    else Icons.Default.Info,
                    contentDescription = if (post.type == PostType.EVENT) stringResource(id = R.string.event)
                    else stringResource(id = R.string.information),
                    tint = if (post.type == PostType.EVENT) AppGreen
                    else AppBlue
                )
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 2
                )
            }
        }



        post.description?.let {
            Text(
                text = it,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp)
            )
        }

        Box(
            modifier = Modifier.fillMaxSize().weight(1f),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                post.authorName?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp, horizontal = 10.dp),
                        fontWeight = if (isAdminPost) FontWeight.W500 else FontWeight.W300,
                        textAlign = TextAlign.End
                    )
                }

                Text(
                    text = SimpleDateFormat("dd/MM/yyyy 'at' HH:mm ", Locale.getDefault()).format(
                        post.date!!
                    ),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    textAlign = TextAlign.End
                )
            }
        }

    }
}


@Composable
fun PostImage(
    modifier: Modifier,
    post: PostResponse,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = post.imageUris!![0],
            contentDescription = stringResource(id = R.string.post_image),
            contentScale = ContentScale.Crop
        )
    }
}
