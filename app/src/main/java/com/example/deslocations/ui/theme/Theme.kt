package com.example.deslocations.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AppLightBlue, //TopBar, Button(container), TextButton(text), ProgressBar,
//     checkBox, OutlinedTextField->focus
    secondary = AppDarkGreen,//AppBar
    tertiary = AppWhite,//-[content color in placeItem]
    background = AppBlack,//Scaffold
    surface = AppBlack,//DropDownMenu
    onPrimary = AppWhite,//Button(text), surface(text,box), tick in Checkbox
    onSecondary = AppWhite, //DesLocations title
    onTertiary = AppWhite, //- [text unselected in map type]
    onBackground = AppWhite,//Scaffold->Text, Surface->Text
    onSurface = AppWhite,//AppBar-> Navigation icon, DropDownMenu->Text, OutlinedTextField->Text
    secondaryContainer = AppTransparentBlue, //FilledTonalButton, [selected item in drawer],
    onSecondaryContainer = AppWhite, //FilledTonalButton Text
    outlineVariant = AppDarkGreen, //Divider
    surfaceVariant = AppLightBlack, //Dialog(Card)container
    onSurfaceVariant = AppLightGray, //Dialog container text(Card), OutlinedTextField center label text
    outline = AppWhiteGray, //Outline Button and TextField border
    primaryContainer = AppBlue,//[chat button container]
    onPrimaryContainer = AppWhite,//[ITEM in place list-> outlinedButton border],[chat button text]
    tertiaryContainer = AppVeryDarkGreen,//[Request item background]
    onTertiaryContainer = AppLightGreen,//[Request item divider color]
    surfaceTint = AppWhite, //DropDown menu(some shadow)
    inverseSurface = AppDarkRed,//[Declined item background]
    inverseOnSurface = AppRed,//[Declined item divider color]
    error = AppErrorDarkRed,
    scrim = Color.Black
//    inversePrimary = Color.Yellow,
//    onError = AppRed,
//    errorContainer = Color.Yellow,
//    onErrorContainer = Color.Yellow,
)

private val LightColorScheme = lightColorScheme(
    primary = AppDarkBlue, //TopBar, Button(container), TextButton(text), ProgressBar,
    // checkBox, OutlinedTextField->focus
    secondary = AppDarkGreen,//AppBar
    tertiary = AppBlack,//-[content color in placeItem]
    background = AppWhite,//Scaffold
    surface = AppDarkWhite,//DropDownMenu
    onPrimary = Color.White,//Button(text), surface(text,box), tick in Checkbox
    onSecondary = Color.White, //DesLocations title
    onTertiary = AppLightBlack, //- [text unselected in map type]
    onBackground = Color.Black,//Scaffold->Text, Surface->Text
    onSurface = Color.Black,//AppBar-> Navigation icon, DropDownMenu->Text, OutlinedTextField->Text
    secondaryContainer = AppTransparentBlue, //FilledTonalButton, [selected item in drawer],
    onSecondaryContainer = AppDarkBlue, //FilledTonalButton Text
    outlineVariant = AppDarkBlue, //Divider
    surfaceVariant = AppLightWhite, //Dialog(Card)container
    onSurfaceVariant = AppGray, //Dialog container text(Card), OutlinedTextField center label text
    outline = AppDarkGreen, //Outline Button and TextField border
    primaryContainer = AppBlueWhite, //[chat button container]
    onPrimaryContainer = AppDarkGray,//[ITEM in place list-> outlinedButton border],[chat button text]
    tertiaryContainer = AppGreenWhite,//[Request item background]
    onTertiaryContainer = AppGreen,//[Request item divider color]
    surfaceTint = Color.Black, //DropDown menu(some shadow)
    inverseSurface = AppLightRed,//[Declined item background]
    inverseOnSurface = AppRed,//[Declined item divider color]
    error = AppErrorRed,
    scrim = Color.Black

//    inversePrimary = Color.Yellow,
//    onError = Color.Yellow,
//    errorContainer = Color.Yellow,
//    onErrorContainer = Color.Yellow,


)

@Composable
fun DesLocationsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}