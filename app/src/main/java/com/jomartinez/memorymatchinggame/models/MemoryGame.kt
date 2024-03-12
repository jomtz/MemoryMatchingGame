package com.jomartinez.memorymatchinggame.models

import com.jomartinez.memorymatchinggame.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize){

    val cards: List<MemoryCard>
    var numberPairsFound = 0

    private var numberOfFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumberPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it, isFacedUp = false, isMatched = false) }
    }

    fun flipCard(position: Int): Boolean {

        numberOfFlips++
        val card = cards[position]
        var foundMatch = false

        if (indexOfSingleSelectedCard == null){
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFacedUp = !card.isFacedUp
        return foundMatch
    }

    private fun restoreCards() {
        for(card in cards){
            if (!card.isMatched){
                card.isFacedUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier){
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numberPairsFound++
        return true
    }

    fun haveWonGame(): Boolean {
        return numberPairsFound == boardSize.getNumberPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFacedUp
    }

    fun getNumberMoves(): Int {
        return numberOfFlips / 2
    }
}