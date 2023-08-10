package com.example.deslocations.presentation.screens.make_post.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.deslocations.model.PostType

@Composable
fun ToggleButton(
    selectedState: PostType,
    toggleStates: List<PostType>,
    onToggleChange: (PostType) -> Unit,
    selectedBackgroundTint: Color = MaterialTheme.colorScheme.primary,
    unselectedBackgroundTint: Color = Color.Unspecified,
    selectedTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant

) {

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline), RoundedCornerShape(4.dp))
    ) {
        toggleStates.forEachIndexed { _, toggleState ->
            val isSelected = selectedState == toggleState
            val backgroundTint =
                if (isSelected) selectedBackgroundTint else unselectedBackgroundTint
            val textColor = if (isSelected) selectedTextColor else unselectedTextColor

            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .background(backgroundTint, RoundedCornerShape(4.dp))
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .toggleable(
                        value = isSelected,
                        enabled = true,
                        onValueChange = { selected ->
                            if (selected) {
                                onToggleChange(toggleState)
                            }

                        }
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = toggleState.getLabel(context),
                    color = textColor,
                    modifier = Modifier
                        .padding(4.dp),
                )
            }

        }
    }

}