package day04

import com.aoc.day04.addHeatAround
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import com.aoc.day04.createAHeatMap

class Solution04Test {
    @Test
    fun `we are able to create a heat map`() {
        val (n, m) = 2 to 2
        val expectedOutput =  mutableListOf (
            mutableListOf(0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0)
        )

        assertEquals(expectedOutput, createAHeatMap(n, m))
    }

    @Test
    fun `we are able to add heat map around a map`() {
        val (n, m) = 2 to 2
        val initialMap = mutableListOf (
            mutableListOf(0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0)
        )
        val expectedOutputOne = mutableListOf (
            mutableListOf(1, 1, 1, 0),
            mutableListOf(1, 0, 1, 0),
            mutableListOf(1, 1, 1, 0),
            mutableListOf(0, 0, 0, 0)
        )
        val expectedOutputTwo = mutableListOf (
            mutableListOf(1, 2, 2, 1),
            mutableListOf(1, 1, 1, 1),
            mutableListOf(1, 2, 2, 1),
            mutableListOf(0, 0, 0, 0)
        )
        assertEquals(expectedOutputOne,
            addHeatAround(initialMap,0, 0))
        assertEquals(expectedOutputTwo,
            addHeatAround(initialMap,0, 1))
    }

}