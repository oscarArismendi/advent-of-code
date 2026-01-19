package day12

import com.aoc.day12.parseDimensionsAndShapeQuantities
import org.junit.jupiter.api.Test


class Solution12Test {
    val firstEasyDimensionAndQuantities = "4x4: 0 0 0 0 2 0" 
    
    @Test
    fun `We're able to extract the dimension and quantities from the input`(){
        //given
        //when
        val result = firstEasyDimensionAndQuantities.parseDimensionsAndShapeQuantities()
        //then
        val expectedResult = listOf(
            4,
            4,
            listOf(0,0,0,0,2,0)
        )
    }
}