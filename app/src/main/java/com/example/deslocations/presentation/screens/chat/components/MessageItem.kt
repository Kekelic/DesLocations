package com.example.deslocations.presentation.screens.chat.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deslocations.ui.theme.DesLocationsTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun MessageItem(
    message: String,
    authorName: String,
    date: Timestamp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 50.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
    ) {

        Card(
            modifier = Modifier.size(34.dp),
            shape = CircleShape,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = authorName[0].uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        ) {

            Text(
                text = authorName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp),
                style = MaterialTheme.typography.labelMedium
            )
            Card(
                shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
            ) {
                Text(
                    text = message,
                    modifier = Modifier
                        .padding(10.dp),
                )
            }

            Text(
                text = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm ",
                    Locale.getDefault()
                ).format(date.toDate()),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MessageItemPreview() {
    DesLocationsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            MessageItem(
                message = "Hello",
                authorName = "Stjepan",
                date = Timestamp(Date()),
            )
        }
    }
}