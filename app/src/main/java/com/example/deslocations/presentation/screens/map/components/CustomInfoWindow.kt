package com.example.deslocations.presentation.screens.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.deslocations.R
import com.example.deslocations.model.Category
import com.example.deslocations.model.PlaceCategory
import com.example.deslocations.ui.theme.AppTransparentBlack

@Composable
fun CustomInfoWindow(
    title: String? = null,
    category: PlaceCategory? = null,
    categoryName: String? = null,
    address: String? = null,
    distance: Float? = null
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(AppTransparentBlack)
    ) {
        title?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall
            )

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = Category(category).color
            )
        }

        category?.let {
            Text(
                text = if (category == PlaceCategory.OTHER) stringResource(id = R.string.category).lowercase()
                        + ": ${it.getLabel(context)}, $categoryName"
                else stringResource(id = R.string.category).lowercase() + ": ${it.getLabel(context)}",
                color = Color.White,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }


        address?.let {
            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)) {
                Text(
                    text = stringResource(id = R.string.address).lowercase() + ": ",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium

                )
                Text(
                    text = it,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )

            }
        }


        distance?.let {
            Text(
                text = if (it >= 1.0) stringResource(id = R.string.distance).lowercase() + ": $it " + stringResource(
                    id = R.string.km_away
                )
                else stringResource(id = R.string.distance).lowercase() + ": ${(it * 1000).toInt()} " + stringResource(
                    id = R.string.m_away
                ),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelMedium

            )
        }


    }

}