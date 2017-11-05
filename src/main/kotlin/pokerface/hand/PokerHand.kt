package pokerface.hand

import pokerface.card.Card

class PokerHand private constructor(private val cards: List<Card>) {

    fun isSameSuit(): Boolean = with(cards[0]) { cards.all { it.suit === this.suit } }

    fun isConsecutive(): Boolean {
        var max = Integer.MIN_VALUE
        var min = Integer.MAX_VALUE
        var nextMin = Integer.MAX_VALUE
        for(card in cards){
            if(card.value < nextMin && card.value > min){
                nextMin = card.value
            }
            if(card.value < min){
                min = card.value
            }
            if(card.value > max){
                max = card.value
            }
        }
        if(Card.KING == max && Card.ACE == min){
            max = 14
            min = nextMin
        }
        return max - min == cards.size - 1
    }

    companion object {

        fun from(cards: List<Card>): PokerHand {
            if(cards.size != 5){
                throw IllegalArgumentException("A Poker hand can only be made with exactly 5 cards but found ${cards.size} card(s)")
            }
            if(!fromSingleDeck(cards)){
                throw IllegalArgumentException("A Poker hand can only be made from a single deck but there are duplicate cards in this hand")
            }
            return PokerHand(cards)
        }

        private fun fromSingleDeck(cards: List<Card>): Boolean {
            return cards.distinct().size == cards.size
        }
    }
}