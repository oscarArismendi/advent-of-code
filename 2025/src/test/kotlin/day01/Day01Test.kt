package day01

import com.aoc.day01.moveDial
import com.aoc.day01.wrapDial
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Day01Test {
    @Test
    fun firstPart() {
    }

    @Test
    fun secondPart() {
    }

    @Test
    fun main() {
    }

    @Test
    fun `wrap negative values`() {
        // given/when
        val resultMinus1 = wrapDial(-1)
        val resultMinus99 = wrapDial(-99)
        val resultMinus50 = wrapDial(-50)
        // then
        assertEquals(99, resultMinus1)
        assertEquals(1, resultMinus99)
        assertEquals(50, resultMinus50)
    }

    @Test
    fun `wrap values between 0 and 99`() {
        // given/when
        val result0 = wrapDial(0)
        val result50 = wrapDial(50)
        val result99 = wrapDial(99)

        // then
        assertEquals(0, result0)
        assertEquals(50, result50)
        assertEquals(99, result99)
    }

    @Test
    fun `wrap values between 100 and 199`() {
        // given/when
        val result100 = wrapDial(100)
        val result101 = wrapDial(101)
        val result199 = wrapDial(199)

        // then
        assertEquals(0, result100)
        assertEquals(1, result101)
        assertEquals(99, result199)

    }
    @Test
    fun `Invalid throw`() {
        assertThrows<IllegalArgumentException> {
            wrapDial(200)
        }
        assertThrows<IllegalArgumentException> {
            wrapDial(-100)
        }
    }

    @Test
    fun `Test moveDial function`() {
        // Basic movement
        assertEquals(49, moveDial('L', 50, 1))
        assertEquals(51, moveDial('R', 50, 1))

        // Wraps on left
        assertEquals(99, moveDial('L', 0, 1))
        assertEquals(99, moveDial('L', 1, 2))

        // Wraps on right
        assertEquals(0, moveDial('R', 99, 1))
        assertEquals(1, moveDial('R', 98, 3))

        // Steps > 100 (trimmed with %100)
        assertEquals(0, moveDial('L', 50, 150)) // Same as 50 - (150 % 100) = 50 - 50 = 0
        assertEquals(0, moveDial('R', 50, 250)) // Same as 50 + (250 % 100) = 50 + 50 = 100 -> 0 after wrapping
    }
}
