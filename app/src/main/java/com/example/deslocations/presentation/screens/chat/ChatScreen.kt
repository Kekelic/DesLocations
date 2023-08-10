package com.example.deslocations.presentation.screens.chat

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.deslocations.presentation.screens.chat.components.ChatContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    placeID: String,
) {
    val context = LocalContext.current

    var isInitSetupDone by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isInitSetupDone) {
        if (!isInitSetupDone) {
            viewModel.getMessages(placeID)
            viewModel.getPlace(placeID)
            isInitSetupDone = true
        }
    }


    when (val getPlaceResponse = viewModel.getPlaceResponse) {
        is Response.Loading -> {
            LaunchedEffect(Unit) {
                isInitSetupDone = false
            }
            Scaffold {
                Box(modifier = Modifier.padding(it), contentAlignment = Alignment.Center) {
                    ProgressBar()
                }
            }
        }

        is Response.Success -> getPlaceResponse.apply {
            ChatContent(
                place = data!!,
                sendMessage = { messageContent ->
                    viewModel.sendMessage(
                        messageContent = messageContent,
                        placeID = placeID
                    )
                },
                messages = viewModel.messagesLiveData,
                currentUserID = viewModel.currentUser?.uid ?: "",
                sendMessageResponse = viewModel.sendMessageResponse,
            )
        }

        is Response.Failure -> getPlaceResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                Utils.showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
            Scaffold {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(onClick = {
                            viewModel.getMessages(placeID)
                            viewModel.getPlace(placeID)
                        }) {
                            Text(text = stringResource(id = R.string.refresh))
                        }
                    }
                }
            }
        }
    }
}