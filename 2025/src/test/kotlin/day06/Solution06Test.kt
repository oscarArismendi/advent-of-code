package day06


import com.aoc.day06.addLineToProblems
import com.aoc.day06.addLineToProblemsAsString
import com.aoc.day06.convertAllProblemsToVerticalNotation
import com.aoc.day06.convertProblemToVerticalNotation
import com.aoc.day06.firstPart
import com.aoc.day06.operateLine
import com.aoc.day06.operateStringLine
import com.aoc.day06.secondPart
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Solution06Test {

    val lineWithoutExtraSpaces = "123 328 51 64"
    val lineWithRandomSpaces = "6 98    215 314"

    @Test
    fun `add line to problems render correctly`(){
        val expected = mutableListOf(
            mutableListOf(123L, 6L),
            mutableListOf(328L, 98L),
            mutableListOf(51L, 215L),
            mutableListOf(64L, 314L)
        )
        val problems = mutableListOf(mutableListOf<Long>())
        addLineToProblems(lineWithoutExtraSpaces, problems)
        addLineToProblems(lineWithRandomSpaces, problems)
        assertEquals(expected,problems)
    }

    @Test
    fun `We are able to sum lines`(){
        val problem = mutableListOf(123L,45L, 6L)
        val result = operateLine("+", problem)
        assertEquals(174L,result)
    }

    @Test
    fun `Multiply line return a correct value`(){
        val problem = mutableListOf(123L,45L, 6L)
        val result = operateLine("*", problem)
        assertEquals(33210L,result)
    }

    @Test
    fun `We're able to add line to problems as strings`(){
        val expected = mutableListOf(
            mutableListOf("123", "6"),
            mutableListOf("328", "98"),
            mutableListOf("51", "215"),
            mutableListOf("64", "314")
        )
        val problems = mutableListOf(mutableListOf<String>())
        addLineToProblemsAsString(lineWithoutExtraSpaces, problems)
        addLineToProblemsAsString(lineWithRandomSpaces, problems)
        assertEquals(expected,problems)
    }

    @Test
    fun `We are able to convert a problem to vertical notation`(){
        val initial = mutableListOf("123", "6  ")
        val initialCase2 = mutableListOf("64 ", "23 ","314")
        val expected = mutableListOf("3", "2","16")
        val expectedCase2 = mutableListOf("4", "431","623")

        val result = convertProblemToVerticalNotation(initial)
        val resultCase2 = convertProblemToVerticalNotation(initialCase2)

        assertEquals(expected,result)
        assertEquals(expectedCase2,resultCase2)
    }

    @Test
    fun `We are able to convert a problems string list to one in cephalopods notation`(){
        val initial = mutableListOf(
            mutableListOf("123", "6"),
            mutableListOf("328", "98"),
            mutableListOf("51", "215"),
            mutableListOf("64", "314")
        )
        val expected = mutableListOf(
            mutableListOf("36", "2","1"),
            mutableListOf("88", "29", "3"),
            mutableListOf("15", "51", "2"),
            mutableListOf("44", "61","3")
        )
        convertAllProblemsToVerticalNotation(initial)
        assertEquals(expected, initial)
    }

    @Test
    fun `We are able to sum string lines`(){
        val problem = mutableListOf("123","45", "6")
        val result = operateStringLine("+", problem)
        assertEquals(174L,result)
    }

    @Test
    fun `We are able to multiply string lines`(){
        val problem = mutableListOf("123","45", "6")
        val result = operateStringLine("*", problem)
        assertEquals(33210L,result)
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
        val inputLines = File("src/main/kotlin/day06/input.txt").readLines()

        // Capture the output of firstPart
        val output = captureOutput { firstPart(inputLines) }

        // Extract the answer from the output
        val ansPattern = "ans: (\\d+)".toRegex()
        val matchResult = ansPattern.find(output)
        val actualAnswer = matchResult?.groupValues?.get(1)

        // Verify the result contains the expected answer
        assertEquals("6295830249262", actualAnswer)
    }

    @Test
    fun `secondPart produces correct result for input file`() {
        // Read the input file
        val inputLines = File("src/main/kotlin/day06/input.txt").readLines()

        // Capture the output of secondPart
        val output = captureOutput { secondPart(inputLines) }

        // Verify the result contains the expected answer
        assertTrue(output.contains("ans: 9194682052782"))
    }
}
