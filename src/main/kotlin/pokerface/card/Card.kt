package pokerface.card

class Card private constructor(val value: Int, val suit: Suit) {

    enum class Suit {
        HEARTS, SPADES, CLUBS, DIAMONDS
    }

    companion object {
        val ACE = 1
        val KING = 13
        val QUEEN = 12
        val JACK = 11

        fun from(value: Int, suit: Suit): Card {
            if(value !in ACE..KING){
                throw IllegalArgumentException("Card value may only be between Ace ($ACE) and King ($KING), provided value $value is invalid")
            }
            return Card(value, suit)
        }

        fun parse(cardRepresentation: String): Card {
            if(cardRepresentation.length != 2){
                throw CardParseException("Can not parse string '$cardRepresentation', must be exactly 2 characters long")
            }
            return Card.from(valueFrom(cardRepresentation[0]), suitFrom(cardRepresentation[1]))
        }

        private fun valueFrom(character: Char): Int {
            return when(character.toUpperCase()){
                in '2'..'9' -> character.toInt() - 48
                'T' -> 10
                'J' -> 11
                'Q' -> 12
                'K' -> 13
                'A' -> 1
                else -> throw CardParseException("Character '$character' is not a valid card value")
            }
        }

        private fun suitFrom(character: Char): Suit {
            return when(character.toUpperCase()){
                'H' -> Suit.HEARTS
                'D' -> Suit.DIAMONDS
                'S' -> Suit.SPADES
                'C' -> Suit.CLUBS
                else -> throw CardParseException("Character '$character' is not a valid suit value")
            }
        }
    }

    class CardParseException(message: String) : RuntimeException(message)
}