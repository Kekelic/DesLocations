package com.example.deslocations.presentation.screens.account_details.components

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deslocations.exceptions.AuthErrorException
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils
import com.example.deslocations.model.Response
import com.example.deslocations.model.User
import com.example.deslocations.model.response.UserResponse
import com.example.deslocations.presentation.components.ChangePasswordDialog
import com.example.deslocations.presentation.components.ProgressBar

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsContent(
    userResponse: UserResponse,
    changeAccountDetails: (User) -> Unit,
    sendPasswordResetEmail: (String) -> Unit,
    sendPasswordResetEmailResponse: Response<Boolean>,
    resetSendPasswordResetEmailResponse: () -> Unit,
    changeAccountDetailsResponse: Response<Boolean>,
    resetChangeAccountDetailsResponse: () -> Unit
) {

    val context = LocalContext.current

    var firstName by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(userResponse.firstName)) }
    var firstNameErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var lastName by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(userResponse.lastName)) }
    var lastNameErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(userResponse.email)) }
    var emailErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var passwordErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    val keyboard = LocalSoftwareKeyboardController.current

    var passwordIsVisible by rememberSaveable { mutableStateOf(false) }

    var isChangePasswordDialogShown by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(vertical = 20.dp, horizontal = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.account_details),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    firstNameErrorMessage = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.firstName))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                isError = !TextUtils.isEmpty(firstNameErrorMessage),
                supportingText = {
                    if (!TextUtils.isEmpty(firstNameErrorMessage)) {
                        Text(text = firstNameErrorMessage)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    lastNameErrorMessage = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.lastName))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                isError = !TextUtils.isEmpty(lastNameErrorMessage),
                supportingText = {
                    if (!TextUtils.isEmpty(lastNameErrorMessage)) {
                        Text(text = lastNameErrorMessage)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailErrorMessage = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.email))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = !TextUtils.isEmpty(emailErrorMessage),
                supportingText = {
                    if (!TextUtils.isEmpty(emailErrorMessage)) {
                        Text(text = emailErrorMessage)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordErrorMessage = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.current_password))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if (passwordIsVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    val imageVector = if (passwordIsVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                    IconButton(onClick = {
                        passwordIsVisible = !passwordIsVisible
                    }) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = stringResource(id = R.string.visibility)
                        )
                    }

                },
                isError = !TextUtils.isEmpty(passwordErrorMessage),
                supportingText = {
                    if (!TextUtils.isEmpty(passwordErrorMessage)) {
                        Text(text = passwordErrorMessage)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { isChangePasswordDialogShown = true }) {
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    style = MaterialTheme.typography.bodyMedium
                )
            }


            Spacer(modifier = Modifier.height(10.dp))

            FilledTonalButton(
                onClick = {
                    keyboard?.hide()
                    when {
                        TextUtils.isEmpty(firstName.text.trim()) -> {
                            firstNameErrorMessage =
                                context.resources.getString(R.string.please_enter_first_name)
                        }

                        TextUtils.isEmpty(lastName.text.trim()) -> {
                            lastNameErrorMessage =
                                context.resources.getString(R.string.please_enter_last_name)
                        }

                        TextUtils.isEmpty(email.text.trim()) -> {
                            emailErrorMessage =
                                context.resources.getString(R.string.please_enter_email)
                        }

                        TextUtils.isEmpty(password.text.trim()) -> {
                            passwordErrorMessage =
                                context.resources.getString(R.string.please_enter_password)
                        }

                        else -> {
                            changeAccountDetails(
                                User(
                                    firstName.text, lastName.text, email.text, password.text
                                )
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.change_account_details),
                    fontSize = 15.sp
                )
            }

        }
    }

    if (isChangePasswordDialogShown) {
        ChangePasswordDialog(
            onDismiss = { isChangePasswordDialogShown = false },
            userEmail = email.text,
            sendPasswordResetEmail = sendPasswordResetEmail,
            sendPasswordResetEmailResponse = sendPasswordResetEmailResponse,
            resetSendPasswordResetEmailResponse = resetSendPasswordResetEmailResponse
        )
    }

    when (changeAccountDetailsResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> changeAccountDetailsResponse.apply {
            LaunchedEffect(key1 = changeAccountDetailsResponse) {
                if (data == true) {
                    val toastText =
                        context.resources.getString(R.string.account_data_changed_successfully)
                    Utils.showMessage(context, toastText)
                    resetChangeAccountDetailsResponse()
                }
            }
        }

        is Response.Failure -> changeAccountDetailsResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                val authErrorException = AuthErrorException(errorCode, context)
                emailErrorMessage = authErrorException.emailErrorMessage
                passwordErrorMessage = authErrorException.passwordErrorMessage
                if (!TextUtils.isEmpty(authErrorException.specificErrorMessage)) {
                    Utils.showMessage(context, authErrorException.specificErrorMessage)
                }
                resetChangeAccountDetailsResponse()
            }
        }
    }


}