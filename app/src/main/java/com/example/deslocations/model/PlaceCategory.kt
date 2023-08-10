package com.example.deslocations.model

import android.content.Context
import com.example.deslocations.R

enum class PlaceCategory(private val labelID: Int) {
    RESTAURANT(R.string.restaurant),
    MARKET(R.string.market),
    CAFE(R.string.cafe),
    BAKERY(R.string.bakery),
    GYM(R.string.gym),
    STORE(R.string.store),
    HOTEL(R.string.hotel),
    SPORT(R.string.sport),
    HAIR_AND_BEAUTY(R.string.hair_and_beauty),
    OTHER(R.string.other);


    fun getLabel(context: Context) =
        context.getString(labelID)
}
