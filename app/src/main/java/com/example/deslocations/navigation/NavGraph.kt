package com.example.deslocations.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.deslocations.presentation.notification.MY_URI
import com.example.deslocations.presentation.notification.POST_ARG
import com.example.deslocations.presentation.screens.about.AboutScreen
import com.example.deslocations.presentation.screens.account_details.AccountDetailsScreen
import com.example.deslocations.presentation.screens.chat.ChatScreen
import com.example.deslocations.presentation.screens.locations_list.LocationsListScreen
import com.example.deslocations.presentation.screens.make_post.MakePostScreen
import com.example.deslocations.presentation.screens.make_request.MakeRequestScreen
import com.example.deslocations.presentation.screens.map.MapScreen
import com.example.deslocations.presentation.screens.place_details.PlaceDetailsScreen
import com.example.deslocations.presentation.screens.post_details.PostDetailsScreen
import com.example.deslocations.presentation.screens.requests.RequestsScreen
import com.example.deslocations.presentation.screens.sign_in.SignInScreen
import com.example.deslocations.presentation.screens.sign_up.SignUpScreen
import com.example.deslocations.presentation.screens.your_locations.YourLocationsScreen


@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = Screen.SignIn.route) {
            SignInScreen(
                navigateToHomeScreen = {
                    navController.navigate(Screen.Map.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                navigateToSignUpScreen = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                navigateToHomeScreen = {
                    navController.navigate(Screen.Map.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Map.route) {
            MapScreen(
                navigateToPlaceDetailsScreen = { placeID ->
                    navController.navigate("${Screen.PlaceDetails.route}/$placeID")
                },
            )
        }

        composable(route = Screen.LocationsList.route) {
            LocationsListScreen(
                navigateToPlaceDetailsScreen = { placeID ->
                    navController.navigate("${Screen.PlaceDetails.route}/$placeID")
                },
            )
        }

        composable(route = Screen.MakeRequest.route) {
            MakeRequestScreen(
                navigateToMapScreen = {
                    navController.navigate(Screen.Map.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(route = Screen.YourLocations.route) {
            YourLocationsScreen(
                navigateToPlaceDetailsScreen = { placeID ->
                    navController.navigate("${Screen.PlaceDetails.route}/$placeID")
                },
            )
        }

        composable(route = Screen.Requests.route) {
            RequestsScreen()
        }


        composable(route = Screen.About.route) {
            AboutScreen()
        }

        composable(route = Screen.AccountDetails.route) {
            AccountDetailsScreen()
        }

        composable(
            route = "${Screen.PlaceDetails.route}/{place_id}",
            arguments = listOf(
                navArgument("place_id") {
                    type = NavType.StringType
                }
            ),
        ) {
            val placeID = it.arguments?.getString("place_id") ?: ""
            PlaceDetailsScreen(
                placeID = placeID,
                navigateToMakePost = { placeID ->
                    navController.navigate("${Screen.MakePost.route}/$placeID")
                },
                navigateToPostDetailsScreen = { postID ->
                    navController.navigate("${Screen.PostDetails.route}/$postID")
                },
                navigateToChatScreen = { placeID ->
                    navController.navigate("${Screen.Chat.route}/$placeID")
                }
            )
        }

        composable(
            route = "${Screen.MakePost.route}/{place_id}",
            arguments = listOf(
                navArgument("place_id") {
                    type = NavType.StringType
                }
            )
        ) {
            val placeID = it.arguments?.getString("place_id") ?: ""
            MakePostScreen(
                placeID = placeID,
                navigateToPlaceDetailsScreen = { placeID ->
                    navController.navigate("${Screen.PlaceDetails.route}/$placeID") {
                        popUpTo("${Screen.PlaceDetails.route}/{place_id}") {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(
            route = "${Screen.PostDetails.route}/{post_id}",
            arguments = listOf(
                navArgument("post_id") {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "$MY_URI/$POST_ARG={$POST_ARG}" }
            )
        ) {
            val postID = it.arguments?.getString("post_id") ?: ""
            PostDetailsScreen(
                postID = postID,
                navigateToPlaceDetailsScreen = { placeID ->
                    navController.navigate("${Screen.PlaceDetails.route}/$placeID") {
                        popUpTo("${Screen.PlaceDetails.route}/{place_id}") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = "${Screen.Chat.route}/{place_id}",
            arguments = listOf(
                navArgument("place_id") {
                    type = NavType.StringType
                }
            )
        ) {
            val placeID = it.arguments?.getString("place_id") ?: ""
            ChatScreen(
                placeID = placeID,
            )
        }
    }

}