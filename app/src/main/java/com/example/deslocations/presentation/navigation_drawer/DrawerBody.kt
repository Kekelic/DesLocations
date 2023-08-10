package com.example.deslocations.presentation.navigation_drawer

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.deslocations.R
import com.example.deslocations.core.Constants
import com.example.deslocations.core.Utils.Companion.showMessage
import com.example.deslocations.model.Response
import com.example.deslocations.navigation.Screen
import com.example.deslocations.presentation.components.ProgressBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerBody(
    viewModel: DrawerViewModel = hiltViewModel(),
    navController: NavHostController,
    closeNavDrawer: () -> Unit,
    isModerator: Boolean,
) {
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            DrawerMenuItem(
                title = stringResource(id = R.string.map),
                imageVector = Icons.Default.Map,
                contentDescription = stringResource(id = R.string.go_to_map_screen),
                onItemClick = {
                    navController.navigate(Screen.Map.route)
                    closeNavDrawer()
                },
                isOpened = navBackStackEntry?.destination?.route == Screen.Map.route,
                closeNavDrawer = closeNavDrawer
            )
            DrawerMenuItem(
                title = stringResource(id = R.string.location_list),
                imageVector = Icons.Rounded.ViewList,
                contentDescription = stringResource(id = R.string.go_to_locations_screen),
                onItemClick = {
                    navController.navigate(Screen.LocationsList.route)
                    closeNavDrawer()
                },
                isOpened = navBackStackEntry?.destination?.route == Screen.LocationsList.route,
                closeNavDrawer = closeNavDrawer
            )

            DrawerMenuItem(
                title = stringResource(id = R.string.about),
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(id = R.string.go_to_about_screen),
                onItemClick = {
                    navController.navigate(Screen.About.route)
                    closeNavDrawer()
                },
                isOpened = navBackStackEntry?.destination?.route == Screen.About.route,
                closeNavDrawer = closeNavDrawer
            )
            Spacer(modifier = Modifier.height(10.dp))
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.padding(15.dp),
                text = stringResource(id = R.string.my_locations),
                fontWeight = FontWeight.SemiBold
            )
            DrawerMenuItem(
                title = stringResource(id = R.string.locations),
                imageVector = Icons.Default.ShareLocation,
                contentDescription = stringResource(id = R.string.go_to_your_locations_screen),
                onItemClick = {
                    navController.navigate(Screen.YourLocations.route)
                    closeNavDrawer()
                },
                isOpened = navBackStackEntry?.destination?.route == Screen.YourLocations.route,
                closeNavDrawer = closeNavDrawer
            )
            DrawerMenuItem(
                title = stringResource(id = R.string.make_request),
                imageVector = Icons.Default.AddLocationAlt,
                contentDescription = stringResource(id = R.string.go_to_make_request_screen),
                onItemClick = {
                    navController.navigate(Screen.MakeRequest.route)
                    closeNavDrawer()
                },
                isOpened = navBackStackEntry?.destination?.route == Screen.MakeRequest.route,
                closeNavDrawer = closeNavDrawer
            )
            Spacer(modifier = Modifier.height(10.dp))
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.padding(15.dp),
                text = stringResource(id = R.string.profile),
                fontWeight = FontWeight.SemiBold
            )
            DrawerMenuItem(
                title = stringResource(id = R.string.account_details),
                imageVector = Icons.Default.AccountBox,
                contentDescription = stringResource(id = R.string.go_to_account_details_screen),
                onItemClick = {
                    navController.navigate(Screen.AccountDetails.route)
                    closeNavDrawer()
                },
                isOpened = navBackStackEntry?.destination?.route == Screen.AccountDetails.route,
                closeNavDrawer = closeNavDrawer
            )
            DrawerMenuItem(
                title = stringResource(id = R.string.sign_out),
                imageVector = Icons.Default.Logout,
                contentDescription = stringResource(id = R.string.sign_out),
                onItemClick = {
                    viewModel.unsubscribeFromFavorites()
                },
                isOpened = false,
                closeNavDrawer = closeNavDrawer,
                isLoading = viewModel.unsubscribeFromFavoritesResponse != Response.Success(false)
            )
            if (isModerator) {
                Spacer(modifier = Modifier.height(10.dp))
                Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    modifier = Modifier.padding(15.dp),
                    text = stringResource(id = R.string.moderator),
                    fontWeight = FontWeight.SemiBold
                )
                DrawerMenuItem(
                    title = stringResource(id = R.string.requests),
                    imageVector = Icons.Default.Mail,
                    contentDescription = stringResource(id = R.string.go_to_requests_screen),
                    onItemClick = {
                        navController.navigate(Screen.Requests.route)
                        closeNavDrawer()
                    },
                    isOpened = navBackStackEntry?.destination?.route == Screen.Requests.route,
                    closeNavDrawer = closeNavDrawer
                )
            }
        }
    }

    when (val unsubscribeFromFavoritesResponse = viewModel.unsubscribeFromFavoritesResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> unsubscribeFromFavoritesResponse.apply {
            LaunchedEffect(Unit) {
                if (data == true) {
                    viewModel.unsubscribeFromFavoritesResponse = Response.Success(false)
                    viewModel.signOut()
                    closeNavDrawer()
                }
            }
        }

        is Response.Failure -> unsubscribeFromFavoritesResponse.apply {
            LaunchedEffect(e) {
                Log.e(Constants.ERROR_TAG, e.message.toString())
                showMessage(context, context.getString(R.string.error_something_went_wrong))
            }
        }
    }

}

