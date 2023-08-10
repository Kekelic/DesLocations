package com.example.deslocations.presentation.screens.map.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.deslocations.R
import com.google.maps.android.compose.MapType

@Composable
fun MapTypeDialog(
    onDismiss: () -> Unit,
    typeOfMap: MapType,
    changeMapType: (MapType) -> Unit,
    selectedColor: Color = MaterialTheme.colorScheme.secondary,
    unselectedColor: Color = MaterialTheme.colorScheme.onTertiary
) {

    var selectedMapType by rememberSaveable {
        mutableStateOf(typeOfMap)
    }

    Dialog(
        onDismissRequest = { onDismiss() }, properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f),
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.choose_map_type),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier
                            .width(80.dp)
                            .height(100.dp)
                    ) {
                        val isSelected = selectedMapType == MapType.NORMAL
                        val textColor =
                            if (isSelected) selectedColor
                            else unselectedColor
                        val borderStroke =
                            if (isSelected) BorderStroke(2.5.dp, selectedColor)
                            else BorderStroke(0.dp, unselectedColor)


                        Image(
                            painter = painterResource(id = R.drawable.map_type_default),
                            contentDescription = stringResource(id = R.string.default_),
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .border(borderStroke, RoundedCornerShape(4.dp))
                                .clickable { selectedMapType = MapType.NORMAL },
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = stringResource(id = R.string.default_),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W400,
                            color = textColor
                        )
                    }

                    Column(
                        modifier = Modifier
                            .width(80.dp)
                            .height(100.dp)
                    ) {
                        val isSelected = selectedMapType == MapType.HYBRID
                        val textColor =
                            if (isSelected) selectedColor
                            else unselectedColor
                        val borderStroke =
                            if (isSelected) BorderStroke(2.5.dp, selectedColor)
                            else BorderStroke(0.dp, unselectedColor)

                        Image(
                            painter = painterResource(id = R.drawable.map_type_satellite),
                            contentDescription = stringResource(id = R.string.satellite),
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .border(borderStroke, RoundedCornerShape(4.dp))
                                .clickable { selectedMapType = MapType.HYBRID },
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = stringResource(id = R.string.satellite),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W400,
                            color = textColor
                        )
                    }

                    Column(
                        modifier = Modifier
                            .width(80.dp)
                            .height(100.dp)
                    ) {
                        val isSelected = selectedMapType == MapType.TERRAIN
                        val textColor =
                            if (isSelected) selectedColor
                            else unselectedColor
                        val borderStroke =
                            if (isSelected) BorderStroke(2.5.dp, selectedColor)
                            else BorderStroke(0.dp, unselectedColor)

                        Image(
                            painter = painterResource(id = R.drawable.map_type_terrain),
                            contentDescription = stringResource(id = R.string.terrain),
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .border(borderStroke, RoundedCornerShape(4.dp))
                                .clickable { selectedMapType = MapType.TERRAIN },
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = stringResource(id = R.string.terrain),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W400,
                            color = textColor
                        )
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
                            changeMapType(selectedMapType)
                            onDismiss()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            }
        }
    }
}
