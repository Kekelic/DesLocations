package com.example.deslocations.presentation.screens.map.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.deslocations.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapMaker(
    position: LatLng,
    @DrawableRes iconResourceId: Int,
    onInfoWindowClick: () -> Unit,
    title: String = "",
    titleColor: Color = Color.Black,
    titleBorder: Color = Color.White,
    visible: Boolean,
    with: Int = 90,
    height: Int = 135,
    content: @Composable () -> Unit,
) {

    val context = LocalContext.current

    val markerState = rememberMarkerState()
    val textState = rememberMarkerState()

    LaunchedEffect(key1 = position) {
        markerState.position = position
        textState.position = position
    }


    MarkerInfoWindow(
        state = markerState,
        icon = bitmapDescriptorFactory(context, iconResourceId, with, height),
        onInfoWindowClick = {
            onInfoWindowClick()
        },
        visible = visible,
        content = {
            content()
        }
    )

    if (!TextUtils.isEmpty(title)) {
        Marker(
            state = textState,
            icon = setCustomTextIcon(title, titleColor, titleBorder, context),
            visible = visible
        )
    }
}

fun bitmapDescriptorFactory(
    context: Context,
    vectorResId: Int,
    width: Int = 90,
    height: Int = 135,
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, width, height)
    val bm = Bitmap.createBitmap(
        width,
        height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

private fun setCustomTextIcon(
    message: String,
    textColor: Color,
    textBorder: Color,
    context: Context
): BitmapDescriptor {

    val paintText = Paint().apply {
        isFakeBoldText = true
        typeface = ResourcesCompat.getFont(context, R.font.domine_bold)
        color = textColor.hashCode()
        textAlign = Paint.Align.CENTER
        textSize = 42.dp.value
    }
    val paintTextBorder = Paint().apply {
        style = Paint.Style.STROKE
        isFakeBoldText = true
        typeface = ResourcesCompat.getFont(context, R.font.domine_bold)
        color = textBorder.hashCode()
        textAlign = Paint.Align.CENTER
        strokeWidth = 2.5.dp.value
        textSize = 42.dp.value
    }

    val height = 46f
    val width = paintText.measureText(message, 0, message.length) + 2

    val bm = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)

    canvas.drawText(message, width / 2, height - 10, paintText)
    canvas.drawText(message, width / 2, height - 10, paintTextBorder)

    return BitmapDescriptorFactory.fromBitmap(bm)
}