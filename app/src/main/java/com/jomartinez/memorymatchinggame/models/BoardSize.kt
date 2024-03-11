package com.jomartinez.memorymatchinggame.models

enum class BoardSize(val numberCards: Int) {
    EASY(8),
    MEDIUM(18),
    HARD(24);

    fun getWidth(): Int {
        return when (this) {
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }

    fun getHeight(): Int {
        return numberCards / getWidth()
    }

    fun getNumberPairs(): Int {
        return numberCards / 2
    }
}