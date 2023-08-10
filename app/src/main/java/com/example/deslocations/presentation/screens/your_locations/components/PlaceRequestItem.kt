package com.example.deslocations.presentation.screens.your_locations.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.deslocations.R
import com.example.deslocations.model.PlaceCategory
import com.example.deslocations.model.PlaceRequest


@Composable
fun PlaceRequestItem(
    modifier: Modifier,
    placeRequest: PlaceRequest,
    cancelPlaceRequest: (PlaceRequest) -> Unit
) {
    val context = LocalContext.current

    var isCancelPlaceRequestDialogShown by rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.tertiary,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.request_is_pending),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(top = 2.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )

            Divider(
                modifier = Modifier.clip(CircleShape),
                thickness = 3.dp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))

            placeRequest.name?.let {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.location_name).lowercase() + ": ")
                        withStyle(SpanStyle(fontWeight = FontWeight.W900)) {
                            append(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(horizontal = 10.dp, vertical = 2.dp),
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            placeRequest.category?.let {
                Text(
                    text = if (it == PlaceCategory.OTHER) stringResource(id = R.string.category).lowercase()
                            + ": ${it.getLabel(context)}" + placeRequest.categoryName?.let { categoryName -> ", $categoryName" }
                    else stringResource(id = R.string.category).lowercase() + ": ${
                        it.getLabel(
                            context
                        )
                    }",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 2.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            placeRequest.address?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.address).lowercase() + ": ",
                    )
                    Text(
                        text = it,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    isCancelPlaceRequestDialogShown = true
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel_request),
                    fontWeight = FontWeight.SemiBold
                )
            }

        }
    }

    if (isCancelPlaceRequestDialogShown) {
        CancelPlaceRequestDialog(
            onDismiss = { isCancelPlaceRequestDialogShown = false },
            placeRequest = placeRequest,
            cancelPlaceRequest = cancelPlaceRequest
        )
    }
}