package com.example.deslocations.navigation

sealed class Screen(val route: String) {
    object SignIn : Screen("Sign in")

    object SignUp : Screen("Sign up")

    object Map : Screen("Map")

    object LocationsList : Screen("Location list")

    object YourLocations : Screen("Locations")

    object MakeRequest : Screen("Make request")

    object Requests : Screen("Requests")

    object About : Screen("About")

    object AccountDetails : Screen("Account details")

    object PlaceDetails : Screen("Place details")

    object MakePost : Screen("Make post")

    object PostDetails : Screen("Post details")

    object Chat : Screen("Chat")
}