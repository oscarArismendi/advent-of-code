package day02

import com.aoc.day02.getLowerAndUpperBound
import com.aoc.day02.getNextInvalidId
import com.aoc.day02.isIdInvalid
import com.aoc.day02.secondPart
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class SolutionTest {
    @Test
    fun `Discerns invalid ids when partitions equals 2`() {
        assertTrue { isIdInvalid("11") }
        assertTrue { isIdInvalid("1111") }
        assertFalse { isIdInvalid("10") }
        assertFalse { isIdInvalid("9") }

    }

    @Test
    fun `Discerns invalid ids when partitions equals 3`() {
        assertFalse { isIdInvalid("11",3) }
        assertFalse { isIdInvalid("1111",3) }
        assertFalse { isIdInvalid("10",3) }
        assertFalse { isIdInvalid("9",3) }
        assertTrue { isIdInvalid("121212",3) }
        assertTrue { isIdInvalid("111",3) }
        assertTrue { isIdInvalid("999",3) }
        assertTrue { isIdInvalid("333333333",3) }
        assertTrue { isIdInvalid("123123123",3) }
        assertTrue { isIdInvalid("824824824",3) }
        assertTrue { isIdInvalid("343434",3) }
        assertTrue { isIdInvalid("565656",3) }
        assertTrue { isIdInvalid("212121",3) }
    }

    @Test
    fun `Correctly identifies invalid IDs for part 2`() {
        // From range 11-22
        assertTrue(isIdInvalid("11"))
        assertTrue(isIdInvalid("22"))

        // From range 95-115
        assertTrue(isIdInvalid("99"))
        assertTrue(isIdInvalid("111",3))

        // From range 998-1012
        assertTrue(isIdInvalid("999",3))
        assertTrue(isIdInvalid("1010"))

        // From range 1188511880-1188511890
        assertTrue(isIdInvalid("1188511885"))

        // From range 222220-222224
        assertTrue(isIdInvalid("222222"))

        // From range 446443-446449
        assertTrue(isIdInvalid("446446"))

        // From range 38593856-38593862
        assertTrue(isIdInvalid("38593859"))

        // From range 565653-565659
        assertTrue(isIdInvalid("565656",3))

        // From range 824824821-824824827
        assertTrue(isIdInvalid("824824824",3))

        // From range 2121212118-2121212124
        assertTrue(isIdInvalid("2121212121",5))

        // Some valid IDs that should not be detected as invalid
        assertFalse(isIdInvalid("12"))
        assertFalse(isIdInvalid("123"))
        assertFalse(isIdInvalid("1234"))
        assertFalse(isIdInvalid("12345"))
    }

    @Test
    fun `Is able to get the lower and upper bound`() {

        assertEquals(Pair(11.toLong(),22.toLong()),getLowerAndUpperBound("11-22"))
        assertEquals(Pair(95.toLong(),115.toLong()),getLowerAndUpperBound("95-115"))
        assertEquals(Pair(1188511880.toLong(),1188511890.toLong()),getLowerAndUpperBound("1188511880-1188511890"))
    }

    @Test
    fun `Is able to get the next invalid Id`() {
        assertEquals(99.toLong(), getNextInvalidId("95"))
        assertEquals(11.toLong(), getNextInvalidId("3"))
        assertEquals(1010.toLong(), getNextInvalidId("444"))
        assertEquals(100100.toLong(), getNextInvalidId("78847"))
    }

    @Test
    fun `Is able to get the next invalid Id when a partition is specify`() {
        assertEquals(111.toLong(), getNextInvalidId("95",3))
        assertEquals(111.toLong(), getNextInvalidId("3",3))
        assertEquals(555.toLong(), getNextInvalidId("444",3))
        assertEquals(121212.toLong(), getNextInvalidId("111210",3))
        assertEquals(111111.toLong(), getNextInvalidId("111012",3))
        assertEquals(121212.toLong(), getNextInvalidId("111112",3))
        assertEquals(121212.toLong(), getNextInvalidId("121210",3))
        assertEquals(100010001000, getNextInvalidId("1188511880",3))
        assertEquals(100010001000, getNextInvalidId("1188511881",3))
        assertEquals(55555, getNextInvalidId("44480",5))
        assertEquals(555555, getNextInvalidId("444800",6))
        assertEquals(5555555, getNextInvalidId("4448000",7))
        assertEquals(55555555, getNextInvalidId("44480000",8))


    }

    @Test
    fun `Test specific invalid IDs in problematic range`() {
        // Test if 55555 and 66666 are invalid IDs with different partitions
        val is55555InvalidWith5 = isIdInvalid("55555", 5)
        val is66666InvalidWith5 = isIdInvalid("66666", 5)

        // Test getNextInvalidId with different partitions
        val nextInvalidIdFrom44480With5 = getNextInvalidId("44480", 5)

        // Manually check if 55555 and 66666 are in the range
        val range = "44480-68595"
        val (lower, upper) = getLowerAndUpperBound(range)

        // Assert that 55555 and 66666 are invalid with the correct partition
        assertTrue(is55555InvalidWith5, "55555 should be invalid with partition=5")
        assertTrue(is66666InvalidWith5, "66666 should be invalid with partition=5")

        // Assert that 55555 and 66666 are in the range
        assertTrue(55555 >= lower && 55555 <= upper, "55555 should be in the range")
        assertTrue(66666 >= lower && 66666 <= upper, "66666 should be in the range")
    }

    @Test
    fun `Range 44480-68595 is handled correctly`() {
        // We need to manually check each number in the range
        val range = "44480-68595"
        val (lower, upper) = getLowerAndUpperBound(range)
        val invalidIds = mutableListOf<Long>()

        // Check each number in the range
        for (num in lower..upper) {
            val numStr = num.toString()

            // Check if the number is made of a single digit repeated
            if (numStr.all { it == numStr[0] }) {
                invalidIds.add(num)
            }
        }

        val sum = invalidIds.sum()

        // Assert that the sum matches the expected sum
        assertEquals(122221, sum, "Sum of invalid IDs in range $range")

        // Assert that 55555 and 66666 are in the list of invalid IDs
        assertTrue(invalidIds.contains(55555), "55555 should be in the list of invalid IDs")
        assertTrue(invalidIds.contains(66666), "66666 should be in the list of invalid IDs")
    }

    @Test
    fun `Test actual secondPart function with specific ranges`() {
        // Create a test for each range
        val testRanges = listOf(
            "78847-119454",
            "636-933",
            "7143759788-7143793713",
            "9960235-10043487",
            "44480-68595",
            "23468-43311",
            "89-123",
            "785189-1014654",
            "3829443354-3829647366",
            "647009-692765"
        )

        val expectedSums = listOf(
            2482087L,
            2331L,
            7143771437L,
            60105009L,
            122221L,
            33333L,
            210L,
            209749540L,
            7659176591L,
            32847997L
        )

        // Test each range individually
        for (i in testRanges.indices) {
            val range = testRanges[i]
            val expectedSum = expectedSums[i]

            // Call the secondPart function
            val actualSum = secondPart(range)

            // Assert that the actual sum matches the expected sum
            assertEquals(expectedSum, actualSum, "Failed for range: $range")
        }
    }
}
