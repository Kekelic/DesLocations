package com.example.deslocations.presentation.components

import android.content.res.Configuration
import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.deslocations.exceptions.AuthErrorException
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils
import com.example.deslocations.model.Response
import com.example.deslocations.ui.theme.DesLocationsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    userEmail: String,
    sendPasswordResetEmail: (String) -> Unit,
    sendPasswordResetEmailResponse: Response<Boolean>,
    resetSendPasswordResetEmailResponse: () -> Unit
) {
    val context = LocalContext.current

    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(userEmail)) }
    var emailErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
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
                    text = stringResource(id = R.string.send_password_reset_email),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailErrorMessage = ""
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.email)
                        )
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth(),
                    isError = !TextUtils.isEmpty(emailErrorMessage),
                    supportingText = {
                        if (!TextUtils.isEmpty(emailErrorMessage)) {
                            Text(text = emailErrorMessage)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextButton(onClick = {
                        onDismiss()
                    }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }

                    TextButton(onClick = {
                        when {
                            TextUtils.isEmpty(email.text.trim()) -> {
                                emailErrorMessage =
                                    context.resources.getString(R.string.please_enter_email)
                            }

                            else -> {
                                sendPasswordResetEmail(email.text)
                            }
                        }
                    }) {
                        if (sendPasswordResetEmailResponse is Response.Loading) {
                            CircularProgressIndicator(modifier = Modifier.size(28.dp))
                        } else {
                            Text(text = stringResource(id = R.string.send))
                        }
                    }

                }

            }

        }

    }


    when (sendPasswordResetEmailResponse) {
        is Response.Loading -> {}
        is Response.Success -> sendPasswordResetEmailResponse.apply {
            LaunchedEffect(key1 = sendPasswordResetEmailResponse) {
                if (data == true) {
                    Utils.showMessage(
                        context,
                        context.resources.getString(R.string.password_reset_sent_successfully)
                    )
                    resetSendPasswordResetEmailResponse()
                    onDismiss()
                }
            }
        }

        is Response.Failure -> sendPasswordResetEmailResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                val authErrorException = AuthErrorException(errorCode, context)
                emailErrorMessage = authErrorException.emailErrorMessage
                if (!TextUtils.isEmpty(authErrorException.specificErrorMessage)) {
                    Utils.showMessage(context, authErrorException.specificErrorMessage)
                }
                resetSendPasswordResetEmailResponse()
            }
        }
    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ChangePasswordDialogPreview() {
    DesLocationsTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            ChangePasswordDialog(
                onDismiss = {},
                userEmail = "",
                sendPasswordResetEmail = {},
                sendPasswordResetEmailResponse = Response.Success(false),
                resetSendPasswordResetEmailResponse = {}
            )
        }
    }
}