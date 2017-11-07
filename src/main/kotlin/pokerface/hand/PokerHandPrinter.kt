package pokerface.hand

import java.io.InputStream
import java.io.PrintStream

class PokerHandPrinter {

    companion object {

        fun printHandRanksFrom(inputStream: InputStream, outputPrintStream: PrintStream = System.out, errorPrintStream: PrintStream = System.err){
            inputStream.bufferedReader().useLines { lines -> lines.forEach { printHandRankFrom(it, outputPrintStream, errorPrintStream) } }
        }

        fun printHandRankFrom(pokerHandString: String, outputPrintStream: PrintStream = System.out, errorPrintStream: PrintStream = System.err) {
            try {
                val rank = PokerHand.parse(pokerHandString).rank()
                outputPrintStream.println("$pokerHandString => $rank")
            } catch (handParseException: PokerHand.PokerHandParseException) {
                errorPrintStream.println("$pokerHandString => ERROR: ${handParseException.message}")
            }
        }

        fun printAsciiArt(outputPrintStream: PrintStream = System.out){
            outputPrintStream.println(  "  _____   ____  _  ________ _____    ______      _____ ______ \n" +
                                        " |  __ \\ / __ \\| |/ /  ____|  __ \\  |  ____/\\   / ____|  ____|\n" +
                                        " | |__) | |  | | ' /| |__  | |__) | | |__ /  \\ | |    | |__   \n" +
                                        " |  ___/| |  | |  < |  __| |  _  /  |  __/ /\\ \\| |    |  __|  \n" +
                                        " | |    | |__| | . \\| |____| | \\ \\  | | / ____ \\ |____| |____ \n" +
                                        " |_|     \\____/|_|\\_\\______|_|  \\_\\ |_|/_/    \\_\\_____|______|")
        }
    }
}