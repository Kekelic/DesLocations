package com.example.deslocations.presentation.screens.your_locations.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.deslocations.model.PlaceDeclined

@Composable
fun PlaceDeclinedItem(
    modifier: Modifier,
    placeDeclined: PlaceDeclined,
    deletePlaceDeclined: (PlaceDeclined) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.tertiary,
            containerColor = MaterialTheme.colorScheme.inverseSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(top = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.declined),
                    modifier = Modifier.fillMaxWidth(0.9f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall
                )
                IconButton(
                    onClick = { deletePlaceDeclined(placeDeclined) },
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete),
                        modifier = Modifier.size(24.dp),
                    )
                }

            }
            Divider(
                modifier = Modifier.clip(CircleShape),
                thickness = 3.dp,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            placeDeclined.name?.let {
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

            placeDeclined.category?.let {
                Text(
                    text = if (it == PlaceCategory.OTHER) stringResource(id = R.string.category).lowercase()
                            + ": ${it.getLabel(context)}" + placeDeclined.categoryName?.let { categoryName -> ", $categoryName" }
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
            placeDeclined.address?.let {
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

            placeDeclined.reasonForDeclining?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 2.dp)
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
    }
}