@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.example.deslocations.presentation.screens.locations_list.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils
import com.example.deslocations.model.Place
import com.example.deslocations.model.Response
import com.example.deslocations.presentation.components.rememberMutableStateListOfPlace

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun LocationsListContent(
    searchPlaces: (String) -> Unit,
    navigateToPlaceDetailsScreen: (String) -> Unit,
    refreshPlaces: () -> Unit,
    placesResponse: Response<List<Place>>
) {


    val context = LocalContext.current

    val placesList = rememberMutableStateListOfPlace()

    var isInitGetPlaceDone by rememberSaveable {
        mutableStateOf(false)
    }

    var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = placesResponse is Response.Loading,
        onRefresh = { refreshPlaces() })

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                            },
                            modifier = Modifier
                                .weight(0.9f),
                            placeholder = {
                                Text(text = stringResource(id = R.string.search_place))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Search
                            ),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        searchPlaces(searchText.text)
                                    },
                                    modifier = Modifier.padding(end = 10.dp)
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = stringResource(id = R.string.search_place),
                                        modifier = Modifier.size(32.dp)
                                    )


                                }
                            },
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    searchPlaces(searchText.text)
                                }
                            )
                        )

                    }
                }
                if (placesList.isEmpty() && isInitGetPlaceDone) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = stringResource(id = R.string.there_are_no_places))
                        }
                    }
                } else {
                    items(items = placesList, key = { it.id!! }) { place ->
                        PlaceItem(
                            modifier = Modifier
                                .animateItemPlacement(),
                            place = place,
                            navigateToPlaceDetailsScreen = navigateToPlaceDetailsScreen
                        )

                    }
                }
            }
            PullRefreshIndicator(
                refreshing = placesResponse is Response.Loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )

        }

    }

    when (placesResponse) {
        is Response.Loading -> {}
        is Response.Success -> placesResponse.apply {
            LaunchedEffect(key1 = placesResponse) {
                if (data != null) {
                    placesList.clear()
                    placesList.addAll(data)
                    isInitGetPlaceDone = true
                }
            }
        }

        is Response.Failure -> placesResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                Utils.showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }
}