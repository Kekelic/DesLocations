package com.example.deslocations.presentation.screens.map.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.deslocations.R
import com.example.deslocations.model.PlaceCategory

@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    listCheckedCategories: List<PlaceCategory>,
    addCategory: (PlaceCategory) -> Unit,
    removeCategory: (PlaceCategory) -> Unit,
    filterPlaces: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = { onDismiss() }, properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(0.80f),
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.filter_place_category),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    PlaceCategory.values().forEach { placeCategory ->
                        val containsValue = listCheckedCategories.contains(placeCategory)
                        var isChecked by rememberSaveable { mutableStateOf(containsValue) }
                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 5.dp)
                                .clip(CircleShape)
                                .clickable {
                                    isChecked = !isChecked
                                    if (isChecked) {
                                        addCategory(placeCategory)
                                    } else removeCategory(placeCategory)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = null,
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = placeCategory.getLabel(context),
                                color = if (isChecked) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onTertiary,
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }


                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }

                    TextButton(
                        onClick = {
                            filterPlaces()
                            onDismiss()
                        },
                    ) {
                        Text(text = stringResource(id = R.string.save))
                    }

                }

            }


        }
    }

}