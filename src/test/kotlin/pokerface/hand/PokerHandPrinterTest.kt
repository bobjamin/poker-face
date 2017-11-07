package pokerface.hand

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PokerHandPrinterTest {

    @Test
    fun `should print full set of valid hands from file`(){
        val testOutputStream = ByteArrayOutputStream()
        val validPokerHandsFile = PokerHandPrinterTest::class.java.classLoader.getResourceAsStream("validPokerHands.txt")
        PokerHandPrinter.printHandRanksFrom(validPokerHandsFile, PrintStream(testOutputStream))
        val expectedOutput =
                 """AS KS QS JS TS => Royal flush
                    |AH 2H 3H 4H 5H => Straight flush
                    |3C 3D 3H 3S 4D => Four of a kind
                    |7H 7D 7C AS AC => Full house
                    |2C 4C 9C TC QC => Flush
                    |AH JS QD TD KS => Straight
                    |4H 4D 4S AC KD => Three of a kind
                    |7C 7S KD KS QH => Two pair
                    |AC AD 7C 9C 3C => One pair
                    |2D 8D 9C TS KH => High card
                    |""".trimMargin("|")
        assertEquals(expectedOutput, testOutputStream.toString())
        testOutputStream.close()
    }

    @Test
    fun `should succeed when there are errors and print the errors to errorStream`(){
        val testOutputStream = ByteArrayOutputStream()
        val testErrorStream = ByteArrayOutputStream()
        val validPokerHandsFile = PokerHandPrinterTest::class.java.classLoader.getResourceAsStream("invalidPokerHands.txt")
        PokerHandPrinter.printHandRanksFrom(validPokerHandsFile, PrintStream(testOutputStream), PrintStream(testErrorStream))
        val expectedSuccessOutput =
                 """AS KS QS JS TS => Royal flush
                    |3C 3D 3H 3S 4D => Four of a kind
                    |4H 4D 4S AC KD => Three of a kind
                    |7C 7S KD KS QH => Two pair
                    |2D 8D 9C TS KH => High card
                    |""".trimMargin("|")
        val expectedErrorOutput =
                 """AH 2H 1H 4H 5H => ERROR: Cannot parse Poker hand because Character '1' is not a valid card value
                    |7P 7D 7C AS AC => ERROR: Cannot parse Poker hand because Character 'P' is not a valid suit value
                    |2C 4C 9C TC TC => ERROR: A Poker hand can only be made from a single deck but there are duplicate cards in this hand
                    |AH JS QD TD IS => ERROR: Cannot parse Poker hand because Character 'I' is not a valid card value
                    |AC AD 7C 9C 10C => ERROR: Cannot parse string 'AC AD 7C 9C 10C', must be exactly 14 characters long
                    |""".trimMargin("|")
        assertEquals(expectedSuccessOutput, testOutputStream.toString())
        assertEquals(expectedErrorOutput, testErrorStream.toString())
        testOutputStream.close()
        testErrorStream.close()
    }
}