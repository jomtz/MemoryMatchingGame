package com.jomartinez.memorymatchinggame.models

import android.adservices.common.AdTechIdentifier

data class MemoryCard(
    val identifier: Int,
    var isFacedUp: Boolean = false,
    var isMatched: Boolean = false
)