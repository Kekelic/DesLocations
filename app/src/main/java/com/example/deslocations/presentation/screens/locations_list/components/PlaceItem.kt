package com.example.deslocations.presentation.screens.locations_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.deslocations.R
import com.example.deslocations.model.Category
import com.example.deslocations.model.Place
import com.example.deslocations.model.PlaceCategory
import com.example.deslocations.presentation.screens.chat.components.MapDialog
import com.google.android.gms.maps.model.LatLng

@Composable
fun PlaceItem(
    modifier: Modifier,
    place: Place,
    navigateToPlaceDetailsScreen: (String) -> Unit
) {

    val context = LocalContext.current

    val alphaColor = if (isSystemInDarkTheme()) 0.7f else 0.4f

    var isMapDialogShown by rememberSaveable {
        mutableStateOf(false)
    }

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
                .clickable { navigateToPlaceDetailsScreen(place.id!!) },
        ) {

            place.name?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Category(place.category).color.copy(alphaColor))
                        .padding(horizontal = 10.dp)
                        .padding(top = 2.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Divider(
                modifier = Modifier.clip(CircleShape),
                thickness = 3.dp,
                color = Category(place.category).color
            )

            Spacer(modifier = Modifier.height(8.dp))

            place.category?.let {
                Text(
                    text = if (it == PlaceCategory.OTHER) stringResource(id = R.string.category).lowercase()
                            + ": ${it.getLabel(context)}" + place.categoryName?.let { categoryName -> ", $categoryName" }
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
            place.address?.let {
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

            place.distance?.let {
                Text(
                    text = if (it >= 1.0) stringResource(id = R.string.distance).lowercase() + ": $it " + stringResource(
                        id = R.string.km_away
                    )
                    else stringResource(id = R.string.distance).lowercase() + ": ${(it * 1000).toInt()} " + stringResource(
                        id = R.string.m_away
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 2.dp),

                    )
                Spacer(modifier = Modifier.height(8.dp))
            }

            TextButton(
                onClick = {
                    isMapDialogShown = true
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                contentPadding = PaddingValues(horizontal = 5.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.show_address_on_map),
                    fontWeight = FontWeight.Medium
                )
                Image(
                    painter = painterResource(Category(place.category).iconResourceId),
                    contentDescription = stringResource(id = R.string.category),
                    modifier = Modifier.size(32.dp)
                )
            }

        }
    }

    if (isMapDialogShown) {

        MapDialog(
            onDismiss = { isMapDialogShown = false },
            latLng = LatLng(place.latitude!!, place.longitude!!),
            placeName = place.name!!,
            category = place.category!!,
            categoryName = place.categoryName ?: "",
            address = place.address!!,
            distance = place.distance,
        )
    }
}