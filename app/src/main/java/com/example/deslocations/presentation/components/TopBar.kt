package com.example.deslocations.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.deslocations.R
import com.example.deslocations.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    onNavigationIconClick: () -> Unit,
    getModeratorState: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.logo_name),
                style = MaterialTheme.typography.displaySmall
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        navigationIcon = {
            if (currentRoute != Screen.SignIn.route && currentRoute != Screen.SignUp.route) {
                IconButton(onClick = {
                    getModeratorState()
                    onNavigationIconClick()
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(id = R.string.toggle_drawer)
                    )
                }
            }
        },
        scrollBehavior = if (currentRoute == Screen.Map.route) TopAppBarDefaults.pinnedScrollBehavior() else scrollBehavior
    )


}

