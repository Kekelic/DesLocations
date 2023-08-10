package com.example.deslocations.presentation.screens.chat.components

import android.content.res.Configuration
import android.text.TextUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.deslocations.R
import com.example.deslocations.core.Utils
import com.example.deslocations.model.Response
import com.example.deslocations.model.response.MessageResponse
import com.example.deslocations.model.response.PlaceResponse
import com.example.deslocations.ui.theme.AppDarkGreen
import com.example.deslocations.ui.theme.DesLocationsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatContent(
    place: PlaceResponse,
    sendMessage: (String) -> Unit,
    messages: LiveData<List<MessageResponse>>,
    currentUserID: String,
    sendMessageResponse: Response<Boolean>
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    val dataMessages by messages.observeAsState()

    LaunchedEffect(key1 = dataMessages?.size) {
        dataMessages?.size?.let { lazyListState.animateScrollToItem(it) }
    }

    var newMessage by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            place.name?.let {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shadowElevation = 8.dp
                ) {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

            }
            dataMessages?.let {
                if (it.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(id = R.string.no_messages))
                    }

                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Bottom,
                        state = lazyListState
                    ) {
                        items(it, key = { message -> message.id!! }) { message ->
                            if (currentUserID == message.authorID) {
                                MyMessageItem(
                                    message = message.content!!,
                                    date = message.date!!
                                )
                            } else {
                                MessageItem(
                                    message = message.content!!,
                                    authorName = message.authorName!!,
                                    date = message.date!!
                                )
                            }
                        }
                    }

                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shadowElevation = 20.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    OutlinedTextField(
                        value = newMessage,
                        onValueChange = {
                            newMessage = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 1.dp),
                        placeholder = {
                            Text(text = stringResource(id = R.string.send_a_message))
                        },
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (TextUtils.isEmpty(newMessage.text.trim())) {
                                    Utils.showMessage(
                                        context,
                                        context.getString(R.string.message_is_empty)
                                    )
                                } else {
                                    sendMessage(newMessage.text)
                                    newMessage = TextFieldValue("")
                                }
                            }
                        ),
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = {
                            IconButton(onClick = {
                                if (TextUtils.isEmpty(newMessage.text.trim())) {
                                    Utils.showMessage(
                                        context,
                                        context.getString(R.string.message_is_empty)
                                    )
                                } else {
                                    sendMessage(newMessage.text)
                                    newMessage = TextFieldValue("")
                                }
                            }) {
                                if (sendMessageResponse is Response.Loading) {
                                    CircularProgressIndicator()
                                } else {
                                    Icon(
                                        imageVector = Icons.Rounded.Send,
                                        contentDescription = stringResource(id = R.string.send),
                                        tint = AppDarkGreen
                                    )
                                }

                            }
                        }
                    )

                }

            }
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ChatContentPreview() {
    DesLocationsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            ChatContent(
                place = PlaceResponse(name = "Konzum shop"),
                sendMessage = {},
                messages = MutableLiveData(),
                currentUserID = "",
                sendMessageResponse = Response.Success(true)

            )
        }
    }
}