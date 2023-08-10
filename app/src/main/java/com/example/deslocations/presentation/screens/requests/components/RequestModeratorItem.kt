@file:OptIn(ExperimentalFoundationApi::class)

package com.example.deslocations.presentation.screens.requests.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deslocations.R
import com.example.deslocations.model.Category
import com.example.deslocations.model.PlaceCategory
import com.example.deslocations.model.PlaceRequest
import com.example.deslocations.presentation.screens.chat.components.MapDialog
import com.example.deslocations.ui.theme.AppGreen
import com.example.deslocations.ui.theme.AppRed
import com.example.deslocations.ui.theme.DesLocationsTheme
import com.google.android.gms.maps.model.LatLng

@ExperimentalFoundationApi
@Composable
fun RequestModeratorItem(
    modifier: Modifier,
    placeRequest: PlaceRequest,
    makePlace: (PlaceRequest) -> Unit,
    declinePlaceRequest: (PlaceRequest, String) -> Unit
) {

    val context = LocalContext.current

    var expanded by rememberSaveable { mutableStateOf(false) }
    var isMapDialogShown by rememberSaveable {
        mutableStateOf(false)
    }
    var isDeclinedReasonDialogShown by rememberSaveable {
        mutableStateOf(false)
    }
    val alphaColor = if (isSystemInDarkTheme()) 0.7f else 0.4f

    Card(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
        ) {

            placeRequest.name?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Category(placeRequest.category).color.copy(alphaColor))
                        .padding(horizontal = 10.dp)
                        .padding(top = 2.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Divider(
                modifier = Modifier.clip(CircleShape),
                thickness = 3.dp,
                color = Category(placeRequest.category).color
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                        .padding(horizontal = 10.dp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            placeRequest.address?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.address).lowercase() + ": ",
                    )
                    Text(
                        text = it,
                    )
                }
            }
            val modifierDetails = if (!expanded) Modifier.size(0.dp) else Modifier
            Column(
                modifier = modifierDetails
                    .padding(horizontal = 10.dp)
                    .animateContentSize(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                placeRequest.adminRole?.let {
                    Text(
                        text = stringResource(id = R.string.role).lowercase() + ": $it",
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                placeRequest.reason?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.reason).lowercase() + ": ",
                        )
                        Text(
                            text = it,
                        )
                    }
                }
            }

            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(0.dp),
                shape = RectangleShape

            ) {
                Text(
                    text = if (expanded) stringResource(id = R.string.hide_details) else stringResource(
                        id = R.string.show_details
                    )
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) stringResource(id = R.string.hide_details) else stringResource(
                        id = R.string.show_details
                    ),
                    modifier = Modifier.size(32.dp)
                )

            }

            TextButton(
                onClick = {
                    isMapDialogShown = true
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(horizontal = 5.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.show_address_on_map),
                    fontWeight = FontWeight.Medium
                )
                Image(
                    painter = painterResource(Category(placeRequest.category).iconResourceId),
                    contentDescription = stringResource(id = R.string.category),
                    modifier = Modifier.size(32.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { makePlace(placeRequest) },
                ) {
                    Text(text = stringResource(id = R.string.approve))
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = stringResource(id = R.string.approve),
                        modifier = Modifier.size(32.dp),
                        tint = AppGreen
                    )
                }

                TextButton(
                    onClick = { isDeclinedReasonDialogShown = true },
                ) {
                    Text(text = stringResource(id = R.string.decline))
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = stringResource(id = R.string.decline),
                        modifier = Modifier.size(32.dp),
                        tint = AppRed
                    )
                }

            }


        }
    }

    if (isMapDialogShown) {
        MapDialog(
            onDismiss = { isMapDialogShown = false },
            latLng = LatLng(placeRequest.latitude!!, placeRequest.longitude!!),
            placeName = placeRequest.name!!,
            category = placeRequest.category!!,
            categoryName = placeRequest.categoryName ?: "",
            address = placeRequest.address!!,
            distance = null
        )
    }

    if (isDeclinedReasonDialogShown) {
        DeclinedReasonDialog(
            onDismiss = { isDeclinedReasonDialogShown = false },
            placeRequest = placeRequest,
            declinePlaceRequest = declinePlaceRequest
        )
    }

}

@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    DesLocationsTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            RequestModeratorItem(
                modifier = Modifier,
                PlaceRequest(
                    name = "Konzum vim",
                    category = PlaceCategory.MARKET,
                    adminRole = "Customer",
                    reason = "Im buying in this shop a lot so i want to be an admin of this place. I think thats okay because noone want to do that.",
                    address = "Vijenac Ivana Meštrovića 90, Osijek, Croatia",
                    latitude = 45.555172320190465,
                    longitude = 18.69739204645157,
                    adminId = "CnK9CfrZxrVS7DDbBw0K5jIKpRY2"

                ),
                makePlace = {},
                declinePlaceRequest = { _, _ -> }
            )
        }
    }
}