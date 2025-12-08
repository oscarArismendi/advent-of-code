package day07

import com.aoc.day07.countTachyonSplits
import com.aoc.day07.firstPart
import com.aoc.day07.secondPart
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Solution07Test {
    @Test
    fun `We're able to count the number of times that a tachyon splits`() {
        val tachyonManifolds = mutableListOf(
            ".......S.......",
            "...............",
            ".......^.......",
            "...............",
            "......^.^......",
            "...............",
        )
        val row = 0
        val column = tachyonManifolds[row].indexOf('S')

        val result = countTachyonSplits(tachyonManifolds,0,Pair(row,column))

        assertEquals(3,result)
    }

    // Capture console output for testing
    private fun captureOutput(block: () -> Unit): String {
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        try {
            block()
            return outputStream.toString().trim()
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `firstPart produces correct result for input file`() {
        // Read the input file
        val inputLines = File("src/main/kotlin/day07/input.txt").readLines().toMutableList()
        
        assertEquals(1658, firstPart(inputLines))
    }

    @Test
    fun `secondPart produces correct result for input file`() {
        // Read the input file
        val inputLines = File("src/main/kotlin/day07/input.txt").readLines().toMutableList()
        
        assertEquals(53916299384254L, secondPart(inputLines))
    }
}
