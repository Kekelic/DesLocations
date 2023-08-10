package com.example.deslocations.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.deslocations.navigation.NavGraph
import com.example.deslocations.navigation.Screen
import com.example.deslocations.presentation.components.TopBar
import com.example.deslocations.presentation.navigation_drawer.DrawerBody
import com.example.deslocations.presentation.navigation_drawer.DrawerHeader
import com.example.deslocations.ui.theme.DesLocationsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            MainContent()
        }
    }

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
    @Composable
    private fun MainContent() {
        DesLocationsTheme {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                        DrawerHeader()
                        DrawerBody(
                            navController = navController,
                            closeNavDrawer = {
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            isModerator = viewModel.isModerator,
                        )
                    }

                },
                gesturesEnabled = drawerState.isOpen
            ) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopBar(
                            navController = navController,
                            onNavigationIconClick = {
                                scope.launch {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    drawerState.open()

                                }
                            },
                            getModeratorState = { viewModel.getModeratorState() },
                            scrollBehavior = scrollBehavior
                        )
                    },
                ) { padding ->
                    val isUserSignedOut = viewModel.getAuthState().collectAsState().value

                    var isSubscribeDone by rememberSaveable {
                        mutableStateOf(false)
                    }

                    LaunchedEffect(isUserSignedOut) {
                        if (!isSubscribeDone && !isUserSignedOut) {
                            viewModel.subscribeToFavorites()
                            isSubscribeDone = true
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        NavGraph(
                            navController = navController,
                            modifier = Modifier,
                            startDestination = if (isUserSignedOut) {
                                Screen.SignIn.route
                            } else Screen.Map.route
                        )
                    }


                }
            }

        }
    }

}



