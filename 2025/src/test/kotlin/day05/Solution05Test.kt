package day05

import com.aoc.day05.firstPart
import com.aoc.day05.secondPart
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class Solution05Test {
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
    fun `firstPart correctly counts fresh ingredients`() {
        // Test data based on inputEasy.txt
        val testInput = listOf(
            "3-5",
            "10-14",
            "16-20",
            "12-18",
            "",
            "1",
            "5",
            "8",
            "11",
            "17",
            "32"
        )
        
        // Expected: ingredients 5, 11, 17 are fresh (within ranges)
        val output = captureOutput { firstPart(testInput) }
        assertTrue(output.contains("First part answer: 3"))
    }
    
    @Test
    fun `secondPart correctly calculates total size of merged ranges`() {
        // Test data with overlapping ranges
        val testInput = listOf(
            "3-5",    // Range 1: size 3
            "10-14",  // Range 2: size 5
            "12-18",  // Overlaps with Range 2, merged: 10-18 (size 9)
            "16-20"   // Overlaps with merged range, final: 10-20 (size 11)
            // Total size after merging: 3 + 11 = 14
        )
        
        val output = captureOutput { secondPart(testInput) }
        assertTrue(output.contains("Second part answer: 14"))
    }
    
    @Test
    fun `firstPart handles edge cases`() {
        // Empty input
        var output = captureOutput { firstPart(listOf()) }
        assertTrue(output.contains("First part answer: 0"))
        
        // No ingredients (only ranges)
        output = captureOutput { firstPart(listOf("3-5", "10-14", "")) }
        assertTrue(output.contains("First part answer: 0"))
        
        // No ranges (only ingredients)
        output = captureOutput { firstPart(listOf("", "5", "11")) }
        assertTrue(output.contains("First part answer: 0"))
    }
    
    @Test
    fun `secondPart handles edge cases`() {
        // Empty input
        var output = captureOutput { secondPart(listOf()) }
        assertTrue(output.contains("Second part answer: 0"))
        
        // Single range
        output = captureOutput { secondPart(listOf("10-20")) }
        assertTrue(output.contains("Second part answer: 11"))
        
        // Adjacent ranges that should merge
        output = captureOutput { secondPart(listOf("10-15", "16-20")) }
        assertTrue(output.contains("Second part answer: 11"))
    }
}