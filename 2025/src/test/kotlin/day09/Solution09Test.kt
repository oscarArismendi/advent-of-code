package day09

import com.aoc.day09.calculateRectangleAreaBetweenTwoCoordinates
import com.aoc.day09.createAListOfAreas
import com.aoc.day09.firstPart
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Solution09Test {
    // Read the input files
    private val inputEasyFile = File("src/main/kotlin/day09/inputEasy.txt")
    private val inputEasyLines = inputEasyFile.readLines().toMutableList()

    @Test
    fun `we are able to calculate the area between any two coordinates`() {
        // Normal case
        val firstCoordinate = 2L to 5L
        val secondCoordinate = 9L to 7L

        val result = calculateRectangleAreaBetweenTwoCoordinates(firstCoordinate, secondCoordinate)

        assertEquals(24L,result)

        // The coordinates share the same x coordinate
        val firstCoordinate2 = 2L to 5L
        val secondCoordinate2 = 2L to 7L

        val result2 = calculateRectangleAreaBetweenTwoCoordinates(firstCoordinate2, secondCoordinate2)

        assertEquals(3L, result2)

        // The coordinates share the same y coordinate
        val firstCoordinate3 = 2L to 3L
        val secondCoordinate3 = 7L to 3L

        val result3 = calculateRectangleAreaBetweenTwoCoordinates(firstCoordinate3, secondCoordinate3)

        assertEquals(6L, result3)

        // Problematic variables
        val firsCoordinate4 = 11L to 1L
        val secondCoordinate4 = 9L to 7L

        val result4 = calculateRectangleAreaBetweenTwoCoordinates(firsCoordinate4, secondCoordinate4)

        assertEquals(21L, result4)
    }

    @Test
    fun `We get a sorted list of areas when we give a list of coordinates`(){
        // Using inputEasy.txt instead of hardcoded values
        val expectedResult = listOf(
            50L, 50L, 40L, 35L, 30L, 30L, 30L, 25L, 24L, 24L, 21L, 21L, 18L, 18L, 15L, 15L, 15L, 15L, 9L, 9L, 8L, 7L, 6L, 5L, 3L, 3L, 3L, 3L
        )

        val result = createAListOfAreas(inputEasyLines)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `Part 1 returns the largest area between any two coordinates`() {
        // Using inputEasy.txt instead of hardcoded values

        // When we run part 1
        val result = firstPart(inputEasyLines)

        // Then we get the largest area
        assertEquals(50L, result)
    }
}
