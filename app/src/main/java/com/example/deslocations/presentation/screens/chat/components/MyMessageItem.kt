package com.example.deslocations.presentation.screens.chat.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deslocations.ui.theme.DesLocationsTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyMessageItem(
    message: String,
    date: Timestamp,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.End
    ) {

        Card(
            shape = RoundedCornerShape(15.dp, 15.dp, 2.dp, 15.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
        ) {
            Text(
                text = message,
                modifier = Modifier
                    .padding(10.dp),
            )
        }

        Text(
            SimpleDateFormat("dd/MM/yyyy HH:mm ", Locale.getDefault()).format(date.toDate()),
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp),
            style = MaterialTheme.typography.labelSmall
        )

    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun YourMessageItemPreview() {
    DesLocationsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            MyMessageItem(
                message = "Hello",
                date = Timestamp(Date()),
            )
        }
    }
}