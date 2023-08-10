package com.example.deslocations.presentation.screens.sign_in

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.R
import com.example.deslocations.core.Constants.ERROR_TAG
import com.example.deslocations.core.Utils.Companion.showMessage
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.presentation.screens.sign_in.components.SignInContent


@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
) {
    val context = LocalContext.current

    SignInContent(
        signIn = { user ->
            viewModel.signIn(user)
        },
        navigateToSignUpScreen = navigateToSignUpScreen,
        sendPasswordResetEmail = { email -> viewModel.sendPasswordResetEmail(email) },
        signInResponse = viewModel.signInResponse,
        currentUser = viewModel.currentUser,
        subscribeToFavorites = { viewModel.subscribeToFavorites() },
        sendPasswordResetEmailResponse = viewModel.sendPasswordResetEmailResponse,
        resetSendPasswordResetEmailResponse = { viewModel.resetSendPasswordResetEmailResponse() }
    )

    when (val subscribeToFavoritesResponse = viewModel.subscribeToFavoritesResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> subscribeToFavoritesResponse.apply {
            LaunchedEffect(Unit) {
                if (data == true) {
                    navigateToHomeScreen()
                }
            }
        }
        is Response.Failure -> subscribeToFavoritesResponse.apply {
            LaunchedEffect(e) {
                Log.e(ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }


}