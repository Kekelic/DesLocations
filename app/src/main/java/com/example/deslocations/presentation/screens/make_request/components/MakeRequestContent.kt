package com.example.deslocations.presentation.screens.make_request.components

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.text.TextUtils
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deslocations.R
import com.example.deslocations.model.Category
import com.example.deslocations.model.PlaceCategory
import com.example.deslocations.model.PlaceRequest
import com.example.deslocations.presentation.screens.map.components.bitmapDescriptorFactory
import com.example.deslocations.ui.theme.AppDarkGray
import com.example.deslocations.ui.theme.DesLocationsTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.firebase.auth.FirebaseUser
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class
)
@Composable
fun MakeRequestContent(
    makeRequest: (placeRequest: PlaceRequest) -> Unit,
    userLocation: Location?,
    currentUser: FirebaseUser?
) {
    val context = LocalContext.current
    var name by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var nameErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var expandedCategory by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf<PlaceCategory?>(null) }
    var selectedCategoryErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var categoryName by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var categoryNameErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var role by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var roleErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var reason by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var reasonErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var placeLatLng by rememberSaveable { mutableStateOf<LatLng?>(null) }
    var placeLatLngErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var address by rememberSaveable { mutableStateOf("") }
    var addressErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    val coroutineScope = rememberCoroutineScope()

    var isInitAnimationDone by rememberSaveable {
        mutableStateOf(false)
    }

    val markerState = rememberMarkerState()

    val cameraPositionState = rememberCameraPositionState {
        val europe = LatLng(40.0, 22.0)
        position = CameraPosition.fromLatLngZoom(europe, 1f)
    }

    val uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    val properties: MapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isBuildingEnabled = true,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            )
        )
    }

    var columnScrollingEnabled by remember { mutableStateOf(true) }

    val focusRequester = remember { FocusRequester() }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    val keyboardController = LocalSoftwareKeyboardController.current

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            LaunchedEffect(key1 = sheetState.isVisible) {
                if (!sheetState.isVisible) {
                    keyboardController?.hide()
                } else {
                    keyboardController?.show()
                    focusRequester.requestFocus()
                }
            }
            TextField(
                value = address,
                onValueChange = {
                    address = it
                    addressErrorMessage = ""
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.address)
                    )
                },
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                    }),
                isError = !TextUtils.isEmpty(addressErrorMessage),
                supportingText = {
                    if (!TextUtils.isEmpty(addressErrorMessage)) {
                        Text(text = addressErrorMessage)
                    }
                }
            )
        }
    ) {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState(), enabled = columnScrollingEnabled),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(id = R.string.become_a_location_admin),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameErrorMessage = ""
                    },
                    label = {
                        Text(text = stringResource(id = R.string.location_name))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    isError = !TextUtils.isEmpty(nameErrorMessage),
                    supportingText = {
                        if (!TextUtils.isEmpty(nameErrorMessage)) {
                            Text(text = nameErrorMessage)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(5.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedCategory, onExpandedChange = {
                        expandedCategory = !expandedCategory
                    },
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedCategory?.getLabel(context) ?: "",
                        onValueChange = { selectedCategoryErrorMessage = "" },
                        singleLine = true,
                        label = { Text(text = stringResource(id = R.string.category)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedCategory
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        isError = !TextUtils.isEmpty(selectedCategoryErrorMessage),
                        supportingText = {
                            if (!TextUtils.isEmpty(selectedCategoryErrorMessage)) {
                                Text(text = selectedCategoryErrorMessage)
                            }
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false },
                    ) {
                        PlaceCategory.values().forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategory = selectionOption
                                    selectedCategoryErrorMessage = ""
                                    expandedCategory = false
                                },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(id = Category(selectionOption).iconResourceId),
                                        contentDescription = stringResource(
                                            id = R.string.category
                                        ),
                                        modifier = Modifier.size(42.dp)
                                    )
                                },
                                contentPadding = PaddingValues(start = 10.dp, top = 5.dp),
                                text = {
                                    Text(text = selectionOption.getLabel(context))
                                }
                            )
                        }
                    }


                }

                if (selectedCategory == PlaceCategory.OTHER) {
                    OutlinedTextField(
                        value = categoryName,
                        onValueChange = {
                            categoryName = it
                            categoryNameErrorMessage = ""
                        },
                        label = {
                            Text(
                                text = stringResource(id = R.string.category_name)
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        isError = !TextUtils.isEmpty(categoryNameErrorMessage),
                        supportingText = {
                            if (!TextUtils.isEmpty(categoryNameErrorMessage)) {
                                Text(text = categoryNameErrorMessage)
                            }
                        }

                    )

                }

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = role,
                    onValueChange = {
                        role = it
                        roleErrorMessage = ""
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.your_role_at_this_location)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    isError = !TextUtils.isEmpty(roleErrorMessage),
                    supportingText = {
                        if (!TextUtils.isEmpty(roleErrorMessage)) {
                            Text(text = roleErrorMessage)
                        }
                    }

                )

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = reason,
                    onValueChange = {
                        reason = it
                        reasonErrorMessage = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    label = {
                        Text(
                            text = stringResource(id = R.string.reason_for_this_request)
                        )
                    },
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    isError = !TextUtils.isEmpty(reasonErrorMessage),
                    supportingText = {
                        if (!TextUtils.isEmpty(reasonErrorMessage)) {
                            Text(text = reasonErrorMessage)
                        }
                    }

                )

                Spacer(modifier = Modifier.height(10.dp))

                GoogleMap(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .height(250.dp)
                    .border(
                        width = 1.dp,
                        color = if (TextUtils.isEmpty(placeLatLngErrorMessage) && TextUtils.isEmpty(
                                addressErrorMessage
                            )
                        )
                            MaterialTheme.colorScheme.outline
                        else MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .motionEventSpy {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                columnScrollingEnabled = false
                            }

                            MotionEvent.ACTION_UP -> {
                                columnScrollingEnabled = true
                            }
                        }
                    },
                    cameraPositionState = cameraPositionState,
                    properties = properties,
                    uiSettings = uiSettings,
                    onMapClick = {
                        placeLatLng = it
                        placeLatLngErrorMessage = ""
                        address = createAddress(it, context)
                        markerState.position = placeLatLng!!
                        markerState.showInfoWindow()
                        coroutineScope.launch {
                            animateToNewLocation(
                                cameraPositionState = cameraPositionState,
                                location = Location(LocationManager.GPS_PROVIDER).apply {
                                    latitude = it.latitude
                                    longitude = it.longitude
                                },
                                durationMs = 500,
                                zoom = cameraPositionState.position.zoom
                            )
                        }
                    },
                    onMapLoaded = {
                        coroutineScope.launch {
                            if (userLocation != null && !isInitAnimationDone) {
                                isInitAnimationDone = true
                                animateToNewLocation(
                                    cameraPositionState = cameraPositionState,
                                    location = userLocation,
                                    durationMs = 1000,
                                )
                            }
                        }
                    }
                ) {
                    if (placeLatLng != null) {
                        MarkerInfoWindow(
                            state = markerState,
                            icon = bitmapDescriptorFactory(
                                context,
                                Category(selectedCategory).iconResourceId
                            ),
                            onInfoWindowClick = {
                                coroutineScope.launch {
                                    sheetState.show()
                                }
                            },
                            content = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    TextField(
                                        value = address,
                                        onValueChange = {},
                                        label = {
                                            Text(
                                                text = stringResource(id = R.string.address)
                                            )
                                        },
                                        colors = TextFieldDefaults.textFieldColors(
                                            containerColor = AppDarkGray,
                                            textColor = MaterialTheme.colorScheme.onPrimary,
                                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                                        ),
                                    )
                                }

                            }
                        )

                        LaunchedEffect(key1 = address) {
                            markerState.showInfoWindow()
                        }
                    }
                }
                if (!TextUtils.isEmpty(placeLatLngErrorMessage)) {
                    Text(
                        text = placeLatLngErrorMessage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (!TextUtils.isEmpty(addressErrorMessage)) {
                    Text(
                        text = addressErrorMessage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                FilledTonalButton(
                    onClick = {
                        when {
                            TextUtils.isEmpty(name.text.trim()) -> {
                                nameErrorMessage =
                                    context.resources.getString(R.string.please_enter_location_name)
                            }

                            selectedCategory == null -> {
                                selectedCategoryErrorMessage =
                                    context.resources.getString(R.string.please_select_category)

                            }

                            (selectedCategory == PlaceCategory.OTHER) && TextUtils.isEmpty(
                                categoryName.text.trim()
                            ) -> {
                                categoryNameErrorMessage =
                                    context.resources.getString(R.string.please_enter_category_name)

                            }

                            TextUtils.isEmpty(role.text.trim()) -> {
                                roleErrorMessage =
                                    context.resources.getString(R.string.please_enter_role)

                            }

                            TextUtils.isEmpty(reason.text.trim()) -> {
                                reasonErrorMessage =
                                    context.resources.getString(R.string.please_enter_reason_for_request)

                            }

                            placeLatLng == null -> {
                                placeLatLngErrorMessage =
                                    context.resources.getString(R.string.please_select_location)

                            }

                            TextUtils.isEmpty(address.trim()) -> {
                                addressErrorMessage =
                                    context.resources.getString(R.string.please_enter_address)

                            }

                            else -> {
                                makeRequest(
                                    PlaceRequest(
                                        name = name.text,
                                        category = selectedCategory,
                                        categoryName = categoryName.text,
                                        adminRole = role.text,
                                        reason = reason.text,
                                        address = address,
                                        latitude = placeLatLng!!.latitude,
                                        longitude = placeLatLng!!.longitude,
                                        adminId = currentUser!!.uid

                                    )
                                )
                            }
                        }

                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Text(text = stringResource(id = R.string.make_request), fontSize = 15.sp)
                }
            }
        }
    }
}

private fun createAddress(placeLatLng: LatLng, context: Context): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    var address = ""
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(
            placeLatLng.latitude,
            placeLatLng.longitude,
            1
        ) { addresses ->
            address =
                addresses[0].thoroughfare + " " + addresses[0].featureName + ", " + addresses[0].locality + ", " + addresses[0].countryName
        }
    } else {
        val addresses = geocoder.getFromLocation(placeLatLng.latitude, placeLatLng.longitude, 1)
        address = (addresses?.get(0)?.thoroughfare ?: "") + " " + (addresses?.get(0)?.featureName
            ?: "") + ", " + (addresses?.get(0)?.locality
            ?: "") + ", " + (addresses?.get(0)?.countryName ?: "")
    }
    return address
}

private suspend fun animateToNewLocation(
    cameraPositionState: CameraPositionState,
    location: Location,
    durationMs: Int,
    zoom: Float = 15f,
) {
    cameraPositionState.animate(
        update = CameraUpdateFactory.newCameraPosition(
            CameraPosition(
                LatLng(location.latitude, location.longitude),
                zoom,
                0f,
                0f
            )
        ),
        durationMs = durationMs
    )
}


@Preview(showBackground = true)
@Composable
fun MakeRequestContentPreview() {
    DesLocationsTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            MakeRequestContent(
                makeRequest = { },
                userLocation = null,
                currentUser = null,
            )
        }
    }
}
