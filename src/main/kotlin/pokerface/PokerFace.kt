package pokerface

import pokerface.hand.PokerHandPrinter
import java.io.File

fun main(args: Array<String>){
    PokerHandPrinter.printAsciiArt()
    println()
    when(args.size){
        1 -> PokerHandPrinter.printHandRanksFrom(File(args[0]).inputStream())
        5 -> PokerHandPrinter.printHandRankFrom(args.joinToString(" "))
        else -> {
            println("Please provide a single argument with the location of a file containing poker hands")
            println("Alternatively, provide 5 card strings for a single poker hand")
        }
    }
}