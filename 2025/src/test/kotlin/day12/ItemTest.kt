package day12

import com.aoc.day12.Item
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ItemTest {
    val firstRawItem = mutableListOf(
        "###",
        "##.",
        "##.",
    )
    @Test
    fun `We're able to parse a single item`(){
        //given
        // when
        val result = Item(firstRawItem)
        // then
        val expectedResult = setOf(
            Pair(0, 0),
            Pair(0,1),
            Pair(0, 2),
            Pair(1, 0),
            Pair(1, 1),
            Pair(2, 0),
            Pair(2, 1),
        )
        assertEquals(expectedResult, result.usedCells)
    }
    
    @Test
    fun `We're able to rotate an item`(){
        //given
        // when
        val firstItem = Item(firstRawItem)
        val firstItemRotated90Degrees = firstItem.rotate(Item.RotationDegrees.NINETY)
        val firstItemRotated180Degrees = firstItem.rotate(Item.RotationDegrees.HUNDRED_EIGHTY)
        val firstItemRotated270Degrees = firstItem.rotate(Item.RotationDegrees.TWO_HUNDRED_SEVENTY)
        // then
        /* 
         The first item rotated 90 degrees
          "###",
           "###",
           "..#",
        */
        val expectedResultForA90DegreesRotation = setOf(
            Pair(0, 0),
            Pair(0,1),
            Pair(0, 2),
            Pair(1, 0),
            Pair(1, 1),
            Pair(1, 2),
            Pair(2, 2),
        )
        
        assertEquals(expectedResultForA90DegreesRotation, firstItemRotated90Degrees.usedCells)
        /* 
         The first item rotated 180 degrees
          ".##",
           ".##",
           "###",
        */
        val expectedResultForA180DegreesRotation = setOf(
            Pair(0,1),
            Pair(0, 2),
            Pair(1, 1),
            Pair(1, 2),
            Pair(2, 0),
            Pair(2, 1),
            Pair(2, 2),
        )
        
        assertEquals(expectedResultForA180DegreesRotation, firstItemRotated180Degrees.usedCells)
        /* 
         The first item rotated 270 degrees
          "#..",
          "###",
          "###",
        */
        val expectedResultForA270DegreesRotation = setOf(
            Pair(0,0),
            Pair(1, 0),
            Pair(1, 1),
            Pair(1, 2),
            Pair(2, 0),
            Pair(2, 1),
            Pair(2, 2),
        )
        
        assertEquals(expectedResultForA270DegreesRotation, firstItemRotated270Degrees.usedCells)
        
    }
}