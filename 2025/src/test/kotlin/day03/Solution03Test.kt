package day03

import com.aoc.day03.getMaxLeft
import com.aoc.day03.getMaxRight
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Solution03Test {

    @Test
    fun `Test getMaxLeft function`() {
        // Basic cases
        assertEquals(Pair('9', 0), getMaxLeft("987654321", 0, 9))
        assertEquals(Pair('8', 0), getMaxLeft("87654321", 0, 8))

        // With start and end parameters
        assertEquals(Pair('7', 2), getMaxLeft("987654321", 2, 9))
        assertEquals(Pair('5', 4), getMaxLeft("987654321", 4, 9))

        // When max is 9 (should break early)
        assertEquals(Pair('9', 2), getMaxLeft("129", 0, 3))
    }

    @Test
    fun `Test getMaxRight function`() {
        // Basic cases
        assertEquals(Pair('9', 0), getMaxRight("987654321", 0, 0))
        assertEquals(Pair('8', 0), getMaxRight("87654321", 0, 0))

        // With start and end parameters
        assertEquals(Pair('9', 3), getMaxRight("123987", 5, 3))
        assertEquals(Pair('8', 3), getMaxRight("123876", 5, 3))

        // When max is 9 (should break early)
        assertEquals(Pair('9', 1), getMaxRight("391", 2, 0))
    }
}
