package com.example.deslocations.presentation.screens.sign_up.components

import android.content.res.Configuration
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deslocations.exceptions.AuthErrorException
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils.Companion.showMessage
import com.example.deslocations.model.Response
import com.example.deslocations.model.User
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.ui.theme.DesLocationsTheme
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SignUpContent(
    signUp: (
        user: User
    ) -> Unit,
    navigateBack: () -> Unit,
    signUpResponse: Response<Boolean>,
    currentUser: FirebaseUser?,
    navigateToHomeScreen: () -> Unit,
) {
    val context = LocalContext.current

    var firstName by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var firstNameErrorMessage by rememberSaveable {
        mutableStateOf("")
    }


    var lastName by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var lastNameErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
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
                text = stringResource(id = R.string.create_a_new_account),
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
                    Text(text = stringResource(id = R.string.password))
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
                    IconButton(
                        onClick = {
                            passwordIsVisible = !passwordIsVisible
                        }
                    ) {
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

            Spacer(modifier = Modifier.height(20.dp))

            Button(
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
                            signUp(User(firstName.text, lastName.text, email.text, password.text))
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navigateBack() }) {
                Text(
                    text = stringResource(id = R.string.already_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

    }

    when (signUpResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> LaunchedEffect(Unit) {
            if (currentUser != null) {
                navigateToHomeScreen()
            }
        }

        is Response.Failure -> signUpResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                val authErrorException = AuthErrorException(errorCode, context)
                emailErrorMessage = authErrorException.emailErrorMessage
                passwordErrorMessage = authErrorException.passwordErrorMessage
                if (!TextUtils.isEmpty(authErrorException.specificErrorMessage)) {
                    showMessage(context, authErrorException.specificErrorMessage)
                }
            }
        }
    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun SignUpContentPreview() {
    DesLocationsTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            SignUpContent(
                signUp = {},
                navigateBack = {},
                signUpResponse = Response.Success(false),
                currentUser = null,
                navigateToHomeScreen = {}

            )
        }
    }
}