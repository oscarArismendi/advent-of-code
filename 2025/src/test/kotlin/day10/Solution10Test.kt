package day10

import com.aoc.day10.findMinimumNumberOfPresses
import com.aoc.day10.firstPart
import com.aoc.day10.parseButtonsWiring
import com.aoc.day10.parseLightDiagram
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.util.BitSet

class Solution10Test {
    // Read the input files
    private val inputEasyFile = File("src/main/kotlin/day10/inputEasy.txt")
    private val inputEasyLines = inputEasyFile.readLines().toMutableList()

    private val oneLineInput = mutableListOf(
        inputEasyLines[0] // Just use the first line from inputEasy.txt
    )

    @Test
    fun `We're able to parse light diagrams from the initial input to a list of strings`(){
        // Given the easy input from file

        // When we parse the light diagrams
        val result = parseLightDiagram(inputEasyLines)

        // Then we get the expected BitSets
        val lightDiagram12 = BitSet(3)
        lightDiagram12.set(1)
        lightDiagram12.set(2)
        val lightDiagram3 = BitSet(4)
        lightDiagram3.set(3)
        val lightDiagram1235 = BitSet(6)
        lightDiagram1235.set(1)
        lightDiagram1235.set(2)
        lightDiagram1235.set(3)
        lightDiagram1235.set(5)
        val expected = listOf(
            lightDiagram12,
            lightDiagram3,
            lightDiagram1235
        )

        assertEquals(expected, result)
    }

    @Test
    fun `We're able to parse buttons wiring from the initial input to a list of strings`(){
        // Given one line of input from file
        val lightDiagramSize = 4

        // When we parse the button wirings
        val result = parseButtonsWiring(oneLineInput,lightDiagramSize)

        // Then we get the expected button wirings
        val buttonWired3 = BitSet(lightDiagramSize)
        buttonWired3.set(3)
        val buttonWired13 = BitSet(lightDiagramSize)
        buttonWired13.set(1)
        buttonWired13.set(3)
        val buttonWired2 = BitSet(lightDiagramSize)
        buttonWired2.set(2)
        val buttonWired23 = BitSet(lightDiagramSize)
        buttonWired23.set(2)
        buttonWired23.set(3)
        val buttonWired02 = BitSet(lightDiagramSize)
        buttonWired02.set(0)
        buttonWired02.set(2)
        val buttonWired01 = BitSet(lightDiagramSize)
        buttonWired01.set(0)
        buttonWired01.set(1)

        val expected = mutableListOf(
            mutableListOf(
                buttonWired3,
                buttonWired13,
                buttonWired2,
                buttonWired23,
                buttonWired02,
                buttonWired01
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun `We get the correct number of button pressed`(){
        // Given a light diagram and button wirings
        val lightDiagramSize = 4
        val lightDiagram = BitSet(lightDiagramSize)
        lightDiagram.set(1)
        lightDiagram.set(2)
        val firstButton = BitSet(lightDiagramSize)
        firstButton.set(1)
        val secondButton = BitSet(lightDiagramSize)
        secondButton.set(2)
        val buttonWiring = mutableListOf(
            firstButton,
            secondButton
        )

        // When we find the minimum number of presses
        val result = findMinimumNumberOfPresses(buttonWiring, lightDiagram)

        // Then we get the expected result
        assertEquals(2, result)
    }

    @Test
    fun `Part 1 returns the total minimum number of button presses for all machines`() {
        // Given the easy input with multiple machines from file

        // When we run part 1
        val result = firstPart(inputEasyLines)

        // Then we get the sum of minimum presses for all machines
        // For the first machine: 2 presses
        // For the second machine: 2 presses
        // For the third machine: 3 presses
        // Total: 7 presses
        assertEquals(7, result)
    }
}
