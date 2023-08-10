package com.example.deslocations.presentation.screens.sign_up

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.presentation.screens.sign_up.components.SignUpContent

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit,
    navigateBack: () -> Unit,
) {

    SignUpContent(
        signUp = { user ->
            viewModel.signUp(user)
        },
        navigateBack = navigateBack,
        signUpResponse = viewModel.signUpResponse,
        currentUser = viewModel.currentUser,
        navigateToHomeScreen = navigateToHomeScreen

    )

}