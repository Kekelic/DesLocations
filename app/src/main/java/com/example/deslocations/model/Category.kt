package com.example.deslocations.model

import androidx.compose.ui.graphics.Color
import com.example.deslocations.R
import com.example.deslocations.ui.theme.AppBakery
import com.example.deslocations.ui.theme.AppCafe
import com.example.deslocations.ui.theme.AppGym
import com.example.deslocations.ui.theme.AppHairAndBeauty
import com.example.deslocations.ui.theme.AppHotel
import com.example.deslocations.ui.theme.AppMarket
import com.example.deslocations.ui.theme.AppOther
import com.example.deslocations.ui.theme.AppRestaurant
import com.example.deslocations.ui.theme.AppSport
import com.example.deslocations.ui.theme.AppStore

class Category(placeCategory: PlaceCategory?) {

    var color: Color =
        when (placeCategory) {
            PlaceCategory.RESTAURANT -> AppRestaurant
            PlaceCategory.MARKET -> AppMarket
            PlaceCategory.CAFE -> AppCafe
            PlaceCategory.BAKERY -> AppBakery
            PlaceCategory.GYM -> AppGym
            PlaceCategory.STORE -> AppStore
            PlaceCategory.HOTEL -> AppHotel
            PlaceCategory.SPORT -> AppSport
            PlaceCategory.HAIR_AND_BEAUTY -> AppHairAndBeauty
            else -> AppOther
        }
        private set

    var iconResourceId: Int =
        when (placeCategory) {
            PlaceCategory.RESTAURANT -> R.drawable.restaurant
            PlaceCategory.MARKET -> R.drawable.market
            PlaceCategory.CAFE -> R.drawable.cafe
            PlaceCategory.BAKERY -> R.drawable.bakery
            PlaceCategory.GYM -> R.drawable.gym
            PlaceCategory.STORE -> R.drawable.store
            PlaceCategory.HOTEL -> R.drawable.hotel
            PlaceCategory.SPORT -> R.drawable.sport
            PlaceCategory.HAIR_AND_BEAUTY -> R.drawable.hair_and_beauty
            else -> R.drawable.other
        }
}