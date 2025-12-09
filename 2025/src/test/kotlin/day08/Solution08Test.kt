package day08

import com.aoc.day08.calculateEuclideanDistance
import com.aoc.day08.createInstructionList
import com.aoc.day08.firstPart
import com.aoc.day08.secondPart
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class Solution08Test {
    @Test
    fun `from coordinates we're able to create a instruction list`() {
        val coordinates = mutableListOf(
            "162,817,812",
            "57,618,57",
            "906,360,560"            
        )
        val expectedInstructionList = listOf(
            Pair(787.814064357828,Pair(0,1)),
            Pair(908.7843528582565,Pair(0,2)),
            Pair(1019.9872548223335,Pair(1,2)),
        )

        assertEquals(expectedInstructionList, createInstructionList(coordinates))
    }

    @Test
    fun `We're able to calculate the Euclidean distance between two points`(){
        //Given
        val coordinates1 = "162,817,812"
        val coordinates2 = "57,618,57"
        val coordinates3 = "906,360,560"

        //When
        val resultFrom1To2 = calculateEuclideanDistance(coordinates1, coordinates2)
        val resultFrom1To3 = calculateEuclideanDistance(coordinates1, coordinates3)
        val resultFrom2To3 = calculateEuclideanDistance(coordinates2, coordinates3)

        // Then
        assertEquals(787.814064357828,resultFrom1To2)
        assertEquals(908.7843528582565,resultFrom1To3)
        assertEquals(1019.9872548223335,resultFrom2To3)        
    }

    @Test
    fun `firstPart produces correct result for input file`() {
        // Read the input file
        val inputLines = File("src/main/kotlin/day08/input.txt").readLines().toMutableList()

        // Verify the result is the expected value
        assertEquals(153328, firstPart(inputLines, 1000))
    }

    @Test
    fun `secondPart produces correct result for input file`() {
        // Read the input file
        val inputLines = File("src/main/kotlin/day08/input.txt").readLines().toMutableList()

        // Verify the result is the expected value
        assertEquals(6095621910L, secondPart(inputLines))
    }
}
