package day09

import com.aoc.day09.calculateRectangleAreaBetweenTwoCoordinates
import com.aoc.day09.createAListOfAreas
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Solution09Test {
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
        val initialCoordinates = mutableListOf(
            "7,1",
            "11,1",
            "11,7",
            "9,7"
        )
        val expectedResult= listOf(
            35L,21L,21L,7L,5L,3L
        )
        
        val result = createAListOfAreas(initialCoordinates)
        
        assertEquals(expectedResult, result)
        
    }

}