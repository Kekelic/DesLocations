package com.example.deslocations.presentation.screens.account_details

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.ProgressBar
import com.example.deslocations.presentation.screens.account_details.components.AccountDetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    viewModel: AccountDetailsViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    when (val getUserDataResponse = viewModel.getUserDataResponse) {
        is Response.Loading -> Scaffold {
            Box(modifier = Modifier.padding(it), contentAlignment = Alignment.Center) {
                ProgressBar()
            }
        }

        is Response.Success -> getUserDataResponse.apply {
            AccountDetailsContent(
                userResponse = data!!,
                changeAccountDetails = { user -> viewModel.changeAccountDetails(user) },
                sendPasswordResetEmail = { email -> viewModel.sendPasswordResetEmail(email) },
                sendPasswordResetEmailResponse = viewModel.sendPasswordResetEmailResponse,
                resetSendPasswordResetEmailResponse = { viewModel.resetSendPasswordResetEmailResponse() },
                changeAccountDetailsResponse = viewModel.changeAccountDetailsResponse,
                resetChangeAccountDetailsResponse = { viewModel.resetChangeAccountDetailsResponse() }
            )
        }

        is Response.Failure -> getUserDataResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                Utils.showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
            Scaffold {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        viewModel.getUserData()
                    }) {
                        Text(text = stringResource(id = R.string.refresh))
                    }
                }
            }
        }
    }
}