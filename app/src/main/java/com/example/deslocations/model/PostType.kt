package com.example.deslocations.model

import android.content.Context
import com.example.deslocations.R

enum class PostType(private val labelID: Int) {
    INFORMATION(R.string.information),
    EVENT(R.string.event);

    fun getLabel(context: Context) =
        context.getString(labelID)

    override fun toString(): String {
        return name.lowercase().replaceFirstChar { it.uppercase() }
    }
}