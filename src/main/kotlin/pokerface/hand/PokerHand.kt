package pokerface.hand

import pokerface.card.Card

class PokerHand private constructor(private val cards: List<Card>) {

    enum class Rank {
         FLUSH, STRAIGHT, STRAIGHT_FLUSH, ROYAL_FLUSH, HIGH_CARD, FULL_HOUSE, FOUR_OF_A_KIND, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND;

        override fun toString(): String {
            return super.toString().replace('_', ' ').toLowerCase().capitalize()
        }
    }

    fun rank(): Rank {
        val valueGroups = this.cards.groupBy { it.value }
        val maxGroupSize = valueGroups.values.maxBy { it.size }!!.size
        return when (valueGroups.size) {
            4 -> Rank.ONE_PAIR
            3 -> if(maxGroupSize == 3) Rank.THREE_OF_A_KIND else Rank.TWO_PAIR
            2 -> if(maxGroupSize == 4) Rank.FOUR_OF_A_KIND else Rank.FULL_HOUSE
            5 -> if(this.isConsecutive()){
                    if(this.isSameSuit()){
                        if (cards.any { it.value == Card.ACE } && cards.any { it.value == Card.KING }) Rank.ROYAL_FLUSH
                        else Rank.STRAIGHT_FLUSH
                    }
                    else Rank.STRAIGHT
                }
                else if (this.isSameSuit()) Rank.FLUSH
                else Rank.HIGH_CARD
            else -> throw IllegalStateException("All five cards can not have the same value if they are from the same deck")
        }
    }

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

    operator fun get(index: Int): Card {
        return cards[index]
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

        fun parse(handRepresentation: String): PokerHand {
            if(handRepresentation.length != 14){
                throw PokerHandParseException("Cannot parse string '$handRepresentation', must be exactly 14 characters long")
            }
            val cardRepresentations = handRepresentation.split(' ')
            if(cardRepresentations.size != 5){
                throw PokerHandParseException("Cannot parse string '$handRepresentation', incorrect representation of cards in string")
            }
            try{
                val cards = cardRepresentations.map { Card.parse(it) }
                return PokerHand.from(cards)
            }
            catch(cardParseException: Card.CardParseException){
                throw PokerHandParseException("Cannot parse Poker hand", cardParseException)
            }
        }

        private fun fromSingleDeck(cards: List<Card>): Boolean {
            return cards.distinct().size == cards.size
        }
    }

    class PokerHandParseException : RuntimeException {
        constructor(message: String): super(message)
        constructor(message: String, cause: Exception): super("$message because ${cause.message}", cause)
    }
}