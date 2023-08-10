package com.example.deslocations.presentation.screens.requests.components

import android.text.TextUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.deslocations.R
import com.example.deslocations.model.PlaceRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeclinedReasonDialog(
    onDismiss: () -> Unit,
    placeRequest: PlaceRequest,
    declinePlaceRequest: (PlaceRequest, String) -> Unit,
) {
    val context = LocalContext.current

    var reason by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var reasonErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
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
                    text = placeRequest.name!!,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = reason,
                    onValueChange = {
                        reason = it
                        reasonErrorMessage = ""
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.reason_for_declining_request)
                        )
                    },
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    modifier = Modifier
                        .fillMaxWidth(),
                    isError = !TextUtils.isEmpty(reasonErrorMessage),
                    supportingText = {
                        if (!TextUtils.isEmpty(reasonErrorMessage)) {
                            Text(text = reasonErrorMessage)
                        }
                    }
                )

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
                            if (TextUtils.isEmpty(reason.text.trim())) {
                                reasonErrorMessage =
                                    context.resources.getString(R.string.please_enter_reason_for_declining)
                            } else {
                                declinePlaceRequest(
                                    placeRequest, reason.text
                                )
                            }

                        },
                    ) {
                        Text(text = stringResource(id = R.string.decline_request))
                    }
                }
            }
        }
    }
}