package pokerface.hand

import junit.framework.TestCase.*
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import pokerface.card.Card

class PokerHandTest {

    @Rule
    @JvmField
    var exceptions = ExpectedException.none()!!

    @Test
    fun `should fail if duplicate cards in hand`(){
        exceptions.expect(IllegalArgumentException::class.java)
        exceptions.expectMessage("A Poker hand can only be made from a single deck but there are duplicate cards in this hand")
        val fiveDiamonds = Card(5, Card.Suit.DIAMONDS)
        val otherFiveDiamonds = Card(5, Card.Suit.DIAMONDS)
        PokerHand.from(listOf(
                 Card(2, Card.Suit.DIAMONDS)
                ,Card(3, Card.Suit.DIAMONDS)
                ,Card(4, Card.Suit.DIAMONDS)
                ,fiveDiamonds, otherFiveDiamonds
        ))
    }

    @Test
    fun `should fail if 6 card hand`(){
        exceptions.expect(IllegalArgumentException::class.java)
        exceptions.expectMessage("A Poker hand can only be made with exactly 5 cards but found 6 card(s)")
        PokerHand.from(listOf(
                Card(2, Card.Suit.DIAMONDS)
                ,Card(3, Card.Suit.DIAMONDS)
                ,Card(4, Card.Suit.DIAMONDS)
                ,Card(5, Card.Suit.DIAMONDS)
                ,Card(6, Card.Suit.DIAMONDS)
                ,Card(7, Card.Suit.DIAMONDS)
        ))
    }

    @Test
    fun `should fail if 0 card hand`(){
        exceptions.expect(IllegalArgumentException::class.java)
        exceptions.expectMessage("A Poker hand can only be made with exactly 5 cards but found 0 card(s)")
        PokerHand.from(emptyList())
    }

    @Test
    fun `should create valid hand if 5 card hand from same deck`(){
        assertNotNull(PokerHand.from(listOf(
                Card(2, Card.Suit.DIAMONDS)
                ,Card(3, Card.Suit.DIAMONDS)
                ,Card(4, Card.Suit.DIAMONDS)
                ,Card(5, Card.Suit.DIAMONDS)
                ,Card(6, Card.Suit.DIAMONDS)
        )))
    }

    @Test
    fun `should have same suit when all cards are same suit`(){
        val hand = PokerHand.from(listOf(
                Card(2, Card.Suit.DIAMONDS)
                ,Card(3, Card.Suit.DIAMONDS)
                ,Card(4, Card.Suit.DIAMONDS)
                ,Card(5, Card.Suit.DIAMONDS)
                ,Card(6, Card.Suit.DIAMONDS)
        ))
        assertTrue(hand.isSameSuit())
    }

    @Test
    fun `should not have same suit when all cards are not same suit`(){
        val hand = PokerHand.from(listOf(
                Card(2, Card.Suit.DIAMONDS)
                ,Card(3, Card.Suit.DIAMONDS)
                ,Card(4, Card.Suit.CLUBS)
                ,Card(5, Card.Suit.DIAMONDS)
                ,Card(6, Card.Suit.DIAMONDS)
        ))
        assertFalse(hand.isSameSuit())
    }

    @Test
    fun `should not be consecutive when all cards are not consecutive`(){
        val hand = PokerHand.from(listOf(
                Card(2, Card.Suit.DIAMONDS)
                ,Card(3, Card.Suit.DIAMONDS)
                ,Card(4, Card.Suit.DIAMONDS)
                ,Card(8, Card.Suit.DIAMONDS)
                ,Card(6, Card.Suit.DIAMONDS)
        ))
        assertFalse(hand.isConsecutive())
    }

    @Test
    fun `should be consecutive when all cards are consecutive`(){
        val hand = PokerHand.from(listOf(
                Card(2, Card.Suit.DIAMONDS)
                ,Card(3, Card.Suit.DIAMONDS)
                ,Card(4, Card.Suit.DIAMONDS)
                ,Card(5, Card.Suit.DIAMONDS)
                ,Card(6, Card.Suit.DIAMONDS)
        ))
        assertTrue(hand.isConsecutive())
    }

    @Test
    fun `should be consecutive when all cards are consecutive ace low`(){
        val hand = PokerHand.from(listOf(
                Card(Card.ACE, Card.Suit.DIAMONDS)
                ,Card(2, Card.Suit.DIAMONDS)
                ,Card(3, Card.Suit.DIAMONDS)
                ,Card(4, Card.Suit.DIAMONDS)
                ,Card(5, Card.Suit.DIAMONDS)
        ))
        assertTrue(hand.isConsecutive())
    }


    @Test
    fun `should be consecutive when all cards are consecutive and ace high`(){
        val hand = PokerHand.from(listOf(
                Card(Card.ACE, Card.Suit.HEARTS)
                ,Card(10, Card.Suit.DIAMONDS)
                ,Card(Card.KING, Card.Suit.CLUBS)
                ,Card(Card.JACK, Card.Suit.DIAMONDS)
                ,Card(Card.QUEEN, Card.Suit.DIAMONDS)
        ))
        assertTrue(hand.isConsecutive())
    }

    @Test
    fun `should not be consecutive when all cards are not consecutive and ace high`(){
        val hand = PokerHand.from(listOf(
                Card(Card.ACE, Card.Suit.HEARTS)
                ,Card(10, Card.Suit.DIAMONDS)
                ,Card(Card.KING, Card.Suit.CLUBS)
                ,Card(5, Card.Suit.DIAMONDS)
                ,Card(Card.QUEEN, Card.Suit.DIAMONDS)
        ))
        assertFalse(hand.isConsecutive())
    }

    @Test
    fun `should rank as high card when no other rank`(){
        val hand = PokerHand.from(listOf(
                Card(Card.ACE, Card.Suit.HEARTS)
                ,Card(2, Card.Suit.DIAMONDS)
                ,Card(Card.KING, Card.Suit.CLUBS)
                ,Card(5, Card.Suit.DIAMONDS)
                ,Card(Card.QUEEN, Card.Suit.DIAMONDS)
        ))
        assertEquals(PokerHand.Rank.HIGH_CARD, hand.rank())
    }

    @Test
    fun `should rank as one pair when paired cards`(){
        val hand = PokerHand.from(listOf(
                Card(Card.ACE, Card.Suit.HEARTS)
                ,Card(Card.ACE, Card.Suit.DIAMONDS)
                ,Card(Card.KING, Card.Suit.CLUBS)
                ,Card(5, Card.Suit.DIAMONDS)
                ,Card(Card.QUEEN, Card.Suit.DIAMONDS)
        ))
        assertEquals(PokerHand.Rank.ONE_PAIR, hand.rank())
    }

    @Test
    fun `should rank as two pair when 2 paired cards`(){
        val hand = PokerHand.from(listOf(
                Card(Card.ACE, Card.Suit.HEARTS)
                ,Card(Card.ACE, Card.Suit.DIAMONDS)
                ,Card(Card.KING, Card.Suit.CLUBS)
                ,Card(Card.KING, Card.Suit.DIAMONDS)
                ,Card(Card.QUEEN, Card.Suit.DIAMONDS)
        ))
        assertEquals(PokerHand.Rank.TWO_PAIR, hand.rank())
    }

    @Test
    fun `should rank as three of a kind when 3 matched value cards`(){
        val hand = PokerHand.from(listOf(
                Card(Card.ACE, Card.Suit.HEARTS)
                ,Card(Card.ACE, Card.Suit.DIAMONDS)
                ,Card(Card.ACE, Card.Suit.CLUBS)
                ,Card(Card.KING, Card.Suit.DIAMONDS)
                ,Card(Card.QUEEN, Card.Suit.DIAMONDS)
        ))
        assertEquals(PokerHand.Rank.THREE_OF_A_KIND, hand.rank())
    }

    @Test
    fun `should rank as straight when consecutive but not same suit cards`(){
        val hand = PokerHand.from(listOf(
                Card(Card.ACE, Card.Suit.HEARTS)
                ,Card(2, Card.Suit.DIAMONDS)
                ,Card(3, Card.Suit.CLUBS)
                ,Card(4, Card.Suit.DIAMONDS)
                ,Card(5, Card.Suit.DIAMONDS)
        ))
        assertEquals(PokerHand.Rank.STRAIGHT, hand.rank())
    }

    @Test
    fun `should rank as flush when same suit but not consecutive cards`(){
        val hand = PokerHand.from(listOf(
                Card(7, Card.Suit.DIAMONDS)
                ,Card(2, Card.Suit.DIAMONDS)
                ,Card(6, Card.Suit.DIAMONDS)
                ,Card(4, Card.Suit.DIAMONDS)
                ,Card(5, Card.Suit.DIAMONDS)
        ))
        assertEquals(PokerHand.Rank.FLUSH, hand.rank())
    }

    @Test
    fun `should rank as full house when matched pair and triple cards`(){
        val hand = PokerHand.from(listOf(
                Card(7, Card.Suit.DIAMONDS)
                ,Card(7, Card.Suit.HEARTS)
                ,Card(9, Card.Suit.DIAMONDS)
                ,Card(9, Card.Suit.CLUBS)
                ,Card(9, Card.Suit.SPADES)
        ))
        assertEquals(PokerHand.Rank.FULL_HOUSE, hand.rank())
    }

    @Test
    fun `should rank as four of a kind when matched 4 cards`(){
        val hand = PokerHand.from(listOf(
                Card(7, Card.Suit.DIAMONDS)
                ,Card(9, Card.Suit.HEARTS)
                ,Card(9, Card.Suit.DIAMONDS)
                ,Card(9, Card.Suit.CLUBS)
                ,Card(9, Card.Suit.SPADES)
        ))
        assertEquals(PokerHand.Rank.FOUR_OF_A_KIND, hand.rank())
    }

    @Test
    fun `should rank as straight flush when same suit and consecutive`(){
        val hand = PokerHand.from(listOf(
                Card(7, Card.Suit.CLUBS)
                ,Card(8, Card.Suit.CLUBS)
                ,Card(9, Card.Suit.CLUBS)
                ,Card(10, Card.Suit.CLUBS)
                ,Card(Card.JACK, Card.Suit.CLUBS)
        ))
        assertEquals(PokerHand.Rank.STRAIGHT_FLUSH, hand.rank())
    }

    @Test
    fun `should rank as straight flush when same suit and consecutive when ace low`(){
        val hand = PokerHand.from(listOf(
                Card(Card.ACE, Card.Suit.CLUBS)
                ,Card(2, Card.Suit.CLUBS)
                ,Card(3, Card.Suit.CLUBS)
                ,Card(4, Card.Suit.CLUBS)
                ,Card(5, Card.Suit.CLUBS)
        ))
        assertEquals(PokerHand.Rank.STRAIGHT_FLUSH, hand.rank())
    }

    @Test
    fun `should rank as royal flush when same suit and consecutive when ace high last`(){
        val rank = PokerHand.from(listOf(
                Card(Card.JACK, Card.Suit.HEARTS)
                ,Card(Card.KING, Card.Suit.HEARTS)
                ,Card(10, Card.Suit.HEARTS)
                ,Card(Card.QUEEN, Card.Suit.HEARTS)
                ,Card(Card.ACE, Card.Suit.HEARTS)
        )).rank()
        assertEquals(PokerHand.Rank.ROYAL_FLUSH, rank)
    }

    @Test
    fun `should rank as royal flush when same suit and consecutive when ace high first`(){
        val rank = PokerHand.from(listOf(
                 Card(Card.ACE, Card.Suit.HEARTS)
                ,Card(Card.JACK, Card.Suit.HEARTS)
                ,Card(Card.KING, Card.Suit.HEARTS)
                ,Card(10, Card.Suit.HEARTS)
                ,Card(Card.QUEEN, Card.Suit.HEARTS)
        )).rank()
        assertEquals(PokerHand.Rank.ROYAL_FLUSH, rank)
    }

    @Test
    fun `should parse valid hand string`(){
        val hand = PokerHand.parse("AS JC KH TD 9H")
        val aceOfSpades = hand[0]
        val jackOfClubs = hand[1]
        val kingOfHearts = hand[2]
        val tenOfDiamonds = hand[3]
        val nineOfHearts = hand[4]
        assertEquals(Card.ACE, aceOfSpades.value)
        assertEquals(Card.Suit.SPADES, aceOfSpades.suit)
        assertEquals(Card.JACK, jackOfClubs.value)
        assertEquals(Card.Suit.CLUBS, jackOfClubs.suit)
        assertEquals(Card.KING, kingOfHearts.value)
        assertEquals(Card.Suit.HEARTS, kingOfHearts.suit)
        assertEquals(10, tenOfDiamonds.value)
        assertEquals(Card.Suit.DIAMONDS, tenOfDiamonds.suit)
        assertEquals(9, nineOfHearts.value)
        assertEquals(Card.Suit.HEARTS, nineOfHearts.suit)
    }

    @Test
    fun `should throw parse exception when invalid card in hand string`(){
        val pokerHandExceptionMessage = "Cannot parse Poker hand because Character 'U' is not a valid card value"
        exceptions.expect(PokerHand.PokerHandParseException::class.java)
        exceptions.expectCause(IsInstanceOf.instanceOf(Card.CardParseException::class.java))
        exceptions.expectMessage(pokerHandExceptionMessage)
        PokerHand.parse("AS JC KH TD UH")
    }

    @Test
    fun `should throw parse exception when string too long`(){
        exceptions.expect(PokerHand.PokerHandParseException::class.java)
        exceptions.expectMessage("Cannot parse string 'AS JC KH TD UHflk;fjaslk', must be exactly 14 characters long")
        PokerHand.parse("AS JC KH TD UHflk;fjaslk")
    }

    @Test
    fun `should throw parse exception when string is valid length but incorrect`(){
        exceptions.expect(PokerHand.PokerHandParseException::class.java)
        exceptions.expectMessage("Cannot parse string 'ASH_JC KH TD U', incorrect representation of cards in string")
        PokerHand.parse("ASH_JC KH TD U")
    }

}