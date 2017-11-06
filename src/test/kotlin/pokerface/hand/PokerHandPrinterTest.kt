package pokerface.hand

import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PokerHandPrinterTest {

    @Rule
    @JvmField
    var exceptions = ExpectedException.none()

    @Test
    fun `should print full set of valid hands from file`(){
        val testOutputStream = ByteArrayOutputStream()
        val validPokerHandsFile = PokerHandPrinterTest::class.java.classLoader.getResourceAsStream("validPokerHands.txt");
        PokerHandPrinter.printHandRanksFrom(validPokerHandsFile, PrintStream(testOutputStream))
        val expectedOutput =
                "AS KS QS JS TS -> Royal flush\n" +
                "AH 2H 3H 4H 5H -> Straight flush\n" +
                "3C 3D 3H 3S 4D -> Four of a kind\n" +
                "7H 7D 7C AS AC -> Full house\n" +
                "2C 4C 9C TC QC -> Flush\n" +
                "AH JS QD TD KS -> Straight\n" +
                "4H 4D 4S AC KD -> Three of a kind\n" +
                "7C 7S KD KS QH -> Two pair\n" +
                "AC AD 7C 9C 3C -> One pair\n" +
                "2D 8D 9C TS KH -> High card\n"
        assertEquals(expectedOutput, testOutputStream.toString())
    }
}