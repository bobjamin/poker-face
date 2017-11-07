package pokerface.card

import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException


class CardTest {

    @Rule
    @JvmField
    var exceptions = ExpectedException.none()!!

    @Test
    fun `should create valid cards when value in range`(){
        (Card.ACE..Card.KING).forEach {
            validValue ->
            val card = Card(validValue, Card.Suit.CLUBS)
            assertEquals(validValue, card.value)
            assertEquals(Card.Suit.CLUBS, card.suit)
        }
    }

    @Test
    fun `should fail card creation when value too low`(){
        exceptions.expect(IllegalArgumentException::class.java)
        exceptions.expectMessage("Card value may only be between Ace (${Card.ACE}) and King (${Card.KING}), provided value 0 is invalid")
        Card(0, Card.Suit.HEARTS)
    }

    @Test
    fun `should fail card creation when value too high`(){
        exceptions.expect(IllegalArgumentException::class.java)
        exceptions.expectMessage("Card value may only be between Ace (${Card.ACE}) and King (${Card.KING}), provided value 15 is invalid")
        Card(15, Card.Suit.CLUBS)
    }

    @Test
    fun `should fail to parse card when invalid length string`(){
        exceptions.expect(Card.CardParseException::class.java)
        exceptions.expectMessage("Can not parse string 'PHQ', must be exactly 2 characters long")
        Card.parse("PHQ")
    }

    @Test
    fun `should fail to parse card when invalid value`(){
        exceptions.expect(Card.CardParseException::class.java)
        exceptions.expectMessage("Character 'P' is not a valid card value")
        Card.parse("PH")
    }

    @Test
    fun `should fail to parse card when invalid suit`(){
        exceptions.expect(Card.CardParseException::class.java)
        exceptions.expectMessage("Character 'P' is not a valid suit value")
        Card.parse("TP")
    }

    @Test
    fun `should map correctly with valid card values`(){
       val validValueMappings = mapOf(  "A" to Card.ACE,
                                        "2" to 2,
                                        "3" to 3,
                                        "4" to 4,
                                        "5" to 5,
                                        "6" to 6,
                                        "7" to 7,
                                        "8" to 8,
                                        "9" to 9,
                                        "T" to 10,
                                        "J" to Card.JACK,
                                        "Q" to Card.QUEEN,
                                        "K" to Card.KING )
        val validSuit = "H"
        validValueMappings.forEach { stringValue, expectedValue ->
            println("$stringValue$validSuit should have value $expectedValue")
            assertEquals(expectedValue, Card.parse("$stringValue$validSuit").value)
        }
    }

    @Test
    fun `should map correctly with valid card suits`(){
        val validSuitMappings = mapOf(  "H" to Card.Suit.HEARTS,
                                        "D" to Card.Suit.DIAMONDS,
                                        "S" to Card.Suit.SPADES,
                                        "C" to Card.Suit.CLUBS)
        val validValue = "A"
        validSuitMappings.forEach { stringSuit, expectedSuit ->
            println("$validValue$stringSuit should be $expectedSuit")
            assertEquals(expectedSuit, Card.parse("$validValue$stringSuit").suit)
        }
    }
}