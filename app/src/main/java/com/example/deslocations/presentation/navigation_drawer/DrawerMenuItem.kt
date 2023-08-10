package com.example.deslocations.presentation.navigation_drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.ui.theme.DesLocationsTheme

@Composable
fun DrawerMenuItem(
    title: String,
    imageVector: ImageVector,
    contentDescription: String,
    onItemClick: () -> Unit,
    isOpened: Boolean,
    closeNavDrawer: () -> Unit,
    isLoading: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isOpened) MaterialTheme.colorScheme.secondaryContainer
                else Color.Unspecified
            )
            .clickable {
                if (isOpened) closeNavDrawer()
                else onItemClick()
            }
            .padding(16.dp),

        verticalAlignment = Alignment.CenterVertically

    ) {
        if (isLoading) {
            Box(modifier = Modifier.size(24.dp)) {
                ProgressBar()
            }
        } else {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun DrawerMenuItemPreview() {
    DesLocationsTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            DrawerMenuItem(
                title = "Map",
                imageVector = Icons.Default.Map,
                contentDescription = "Go to map screen",
                onItemClick = {},
                isOpened = true,
                closeNavDrawer = {},
                isLoading = false
            )
        }
    }
}