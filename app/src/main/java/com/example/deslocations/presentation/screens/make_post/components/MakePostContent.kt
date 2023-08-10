package com.example.deslocations.presentation.screens.make_post.components

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.text.TextUtils
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.deslocations.R
import com.example.deslocations.core.ComposeFileProvider
import com.example.deslocations.core.Utils.Companion.showMessage
import com.example.deslocations.model.Post
import com.example.deslocations.model.PostType
import com.example.deslocations.presentation.components.rememberMutableStateListOf
import com.example.deslocations.ui.theme.AppBlack
import com.example.deslocations.ui.theme.AppTransparentWhite
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MakePostContent(
    placeID: String,
    makePost: (Post) -> Unit,
) {
    val context = LocalContext.current
    var title by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var titleErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var selectedPostType by rememberSaveable {
        mutableStateOf(PostType.INFORMATION)
    }

    var startDate by rememberSaveable {
        mutableStateOf(" ")
    }
    var startDateErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var startTime by rememberSaveable {
        mutableStateOf(" ")
    }
    var startTimeErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var description by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var descriptionErrorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var imageFromCameraUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }

    val imageUris = rememberMutableStateListOf<Uri>()


    val multiplePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia()
        ) { uris ->
            imageUris.addAll(0, uris)
        }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUris.add(0, imageFromCameraUri!!)
        }
    }

    val permissionToRequest = Manifest.permission.CAMERA

    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val uri = ComposeFileProvider.getImageUri(context)
                imageFromCameraUri = uri
                cameraLauncher.launch(uri)
            } else {
                showMessage(context, context.resources.getString(R.string.camera_permission_denied))
            }
        }
    )

    val calendar = Calendar.getInstance()
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    val timePickerDialog = TimePickerDialog(
        context, { _, selectedHourOfDay, selectedMinute ->
            startTime = String.format("%02d:%02d", selectedHourOfDay, selectedMinute)
            startTimeErrorMessage = ""
        }, hourOfDay, minute, false
    )

    val datePickerDialog = DatePickerDialog(
        context, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            startDate =
                String.format("%02d/%02d/%02d", selectedDayOfMonth, selectedMonth + 1, selectedYear)
            startDateErrorMessage = ""
        }, year, month, dayOfMonth
    )

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.9f)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = stringResource(id = R.string.new_post),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                                titleErrorMessage = ""
                            },
                            label = {
                                Text(text = stringResource(id = R.string.post_title))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                            modifier = Modifier
                                .fillMaxWidth(),
                            isError = !TextUtils.isEmpty(titleErrorMessage),
                            supportingText = {
                                if (!TextUtils.isEmpty(titleErrorMessage)) {
                                    Text(text = titleErrorMessage)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        ToggleButton(
                            selectedState = selectedPostType,
                            toggleStates = PostType.values().toList(),
                            onToggleChange = {
                                selectedPostType =
                                    if (selectedPostType == PostType.INFORMATION) PostType.EVENT
                                    else PostType.INFORMATION
                            }
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        val modifier =
                            if (selectedPostType == PostType.INFORMATION) Modifier.size(0.dp) else Modifier
                        Row(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(bottom = 5.dp)
                                .animateContentSize(
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessMediumLow
                                    )
                                )
                        ) {
                            OutlinedTextField(
                                value = startDate,
                                onValueChange = {
                                    startDate = it
                                },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.start_date)
                                    )
                                },
                                readOnly = true,
                                interactionSource = remember { MutableInteractionSource() }
                                    .also { interactionSource ->
                                        LaunchedEffect(interactionSource) {
                                            interactionSource.interactions.collect {
                                                if (it is PressInteraction.Release) {
                                                    datePickerDialog.show()
                                                }
                                            }
                                        }

                                    },
                                modifier = Modifier
                                    .weight(0.5f),
                                isError = !TextUtils.isEmpty(startDateErrorMessage),
                                supportingText = {
                                    if (!TextUtils.isEmpty(startDateErrorMessage)) {
                                        Text(text = startDateErrorMessage)
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            OutlinedTextField(
                                value = startTime,
                                onValueChange = {
                                    startTime = it
                                },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.start_time)
                                    )
                                },
                                readOnly = true,
                                interactionSource = remember { MutableInteractionSource() }
                                    .also { interactionSource ->
                                        LaunchedEffect(interactionSource) {
                                            interactionSource.interactions.collect {
                                                if (it is PressInteraction.Release) {
                                                    timePickerDialog.show()
                                                }
                                            }
                                        }

                                    },
                                modifier = Modifier
                                    .weight(0.5f),
                                isError = !TextUtils.isEmpty(startTimeErrorMessage),
                                supportingText = {
                                    if (!TextUtils.isEmpty(startTimeErrorMessage)) {
                                        Text(text = startTimeErrorMessage)
                                    }
                                }
                            )
                        }

                        OutlinedTextField(
                            value = description,
                            onValueChange = {
                                description = it
                                descriptionErrorMessage = ""
                            },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.post_description)
                                )
                            },
                            singleLine = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            isError = !TextUtils.isEmpty(descriptionErrorMessage),
                            supportingText = {
                                if (!TextUtils.isEmpty(descriptionErrorMessage)) {
                                    Text(text = descriptionErrorMessage)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    cameraPermissionResultLauncher.launch(permissionToRequest)
                                },
                            ) {
                                Text(
                                    text = "Camera",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Icon(
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = stringResource(id = R.string.add_photo_from_camera),
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    multiplePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                            ) {
                                Text(
                                    text = "Gallery",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = stringResource(id = R.string.add_photo_from_gallery),
                                    modifier = Modifier.size(26.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
                itemsIndexed(imageUris) { _, uri ->
                    Box(
                        modifier = Modifier
                            .animateItemPlacement()
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.width(280.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Box(contentAlignment = Alignment.TopEnd) {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = stringResource(id = R.string.selected_image),
                                    contentScale = ContentScale.FillBounds,
                                )
                                FilledIconButton(
                                    onClick = {
                                        imageUris.remove(uri)
                                    },
                                    shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 16.dp),
                                    colors = IconButtonDefaults.filledIconButtonColors(
                                        containerColor = AppTransparentWhite,
                                        contentColor = AppBlack,
                                    ),
                                    modifier = Modifier
                                        .size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = stringResource(id = R.string.delete_photo),
                                    )
                                }
                            }
                        }
                    }
                }
            }
            FilledTonalButton(
                onClick = {
                    when {
                        TextUtils.isEmpty(title.text.trim()) -> {
                            titleErrorMessage =
                                context.resources.getString(R.string.please_enter_post_title)
                        }

                        (selectedPostType == PostType.EVENT) && TextUtils.isEmpty(startDate.trim()) -> {
                            startDateErrorMessage =
                                context.resources.getString(R.string.please_select_start_date)
                        }

                        (selectedPostType == PostType.EVENT) && TextUtils.isEmpty(startTime.trim()) -> {
                            startTimeErrorMessage =
                                context.resources.getString(R.string.please_select_start_time)
                        }

                        TextUtils.isEmpty(description.text.trim()) -> {
                            descriptionErrorMessage =
                                context.resources.getString(R.string.please_enter_post_description)
                        }

                        else -> {
                            val post = Post()
                            post.title = title.text
                            post.description = description.text
                            post.placeID = placeID
                            post.imageUris = imageUris
                            post.type = selectedPostType

                            if (selectedPostType == PostType.EVENT) {
                                post.startDate = "$startDate $startTime"
                            }
                            makePost(post)
                        }
                    }


                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.make_post))
            }
        }

    }

}



