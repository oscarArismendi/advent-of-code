package day10

import com.aoc.day10.findMinimumNumberOfPresses
import com.aoc.day10.firstPart
import com.aoc.day10.getValidButtonsCombinations
import com.aoc.day10.heuristic
import com.aoc.day10.parseButtonsWiring
import com.aoc.day10.parseJoltageLevels
import com.aoc.day10.parseLightDiagram
import com.aoc.day10.secondPart
import com.aoc.day10.toLightDiagram
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.util.BitSet
import kotlin.test.assertContentEquals

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

    @Test
    fun `We're able to parse joltage levels from the initial input to a list of Ints`(){
        // Given the easy input from file

        // When we parse the light diagrams
        val result = parseJoltageLevels(inputEasyLines)

        // Then we get the expected BitSets
        val firstJoltageLeves = intArrayOf(3,5,4,7)
        val secondJoltageLeves = intArrayOf(7,5,12,7,2)
        val thirdJoltageLeves = intArrayOf(10,11,11,5,10,5)
        val expected = mutableListOf(
            firstJoltageLeves,
            secondJoltageLeves,
            thirdJoltageLeves
        )

        result.forEachIndexed { index, actualArray ->
            assertContentEquals(expected[index], actualArray)
        }
    }

    @Test
    fun `When the joltage target is 0 for the levels we get zero as the ans`(){
        // Given a joltage level target of 0
        val inputLines = mutableListOf(
            "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {0,0,0,0}"
        )
        // When we get the ans of the second ans
        
        val result = secondPart(inputLines)

        // Then we expect 0 pressed buttons as the answer
        val expected = 0

        assertEquals(expected, result)
    }

    @Test
    fun `When the joltage target can only be reached with one button`(){
        // Given a joltage level target of 0
        val inputLines = mutableListOf(
            "[.##..] (3) (1,3) (2) (2,3) (0,2) (0,1) (4) {0,0,0,0,5}"
        )
        // When we get the ans of the second ans

        val result = secondPart(inputLines)

        // Then we expect 5 pressed buttons as we can only press the (4) button
        val expected = 5

        assertEquals(expected, result)
    }

    @Test
    fun `When the joltage target can only be reached with by duplicated buttons`(){
        // Given a joltage level target of 0
        val inputLines = mutableListOf(
            "[.##..] (3) (1,3) (2) (2,3) (0,2) (4) (4) {0,0,0,0,5}"
        )
        // When we get the ans of the second ans

        val result = secondPart(inputLines)

        // Then we expect 5 pressed buttons as we can only press any of the (4) button
        val expected = 5

        assertEquals(expected, result)
    }

    @Test
    fun `When the joltage target can only be reached with two buttons`(){
        // Given a joltage level target of 0
        val inputLines = mutableListOf(
            "[.##..] (1) (2) (0,2) (3) (4) {0,0,0,5,5}"
        )
        // When we get the ans of the second ans

        val result = secondPart(inputLines)

        // Then we expect 10 pressed buttons as we can only press (4) and (3)
        val expected = 10

        assertEquals(expected, result)
    }

    @Test
    fun `We choose the optimal path for the joltage levels`(){
        // Given a joltage level target of 0
        val inputLines = mutableListOf(
            "[.##..] (1) (2) (0,2) (3,4) (3) (4) {0,0,0,5,5}"
        )
        // When we get the ans of the second part
        val result = secondPart(inputLines)

        // Then we expect 5 pressed buttons as we should only press (3,4) and not (4) or (3)
        val expected = 5

        assertEquals(expected, result)
    }

    @Test
    fun `We choose the optimal path for a combination of two buttons`(){
        // Given a joltage level target of 0
        val inputLines = mutableListOf(
            "[.##..] (1) (2) (0,2) (3,4) (3) (4) {0,0,0,5,6}"
        )
        // When we get the ans of the second part
        val result = secondPart(inputLines)

        // Then we expect 6 pressed buttons as we should only press (3,4) five times and (4) one time 
        val expected = 6

        assertEquals(expected, result)
    }

    @Test
    fun `Part 2 returns the total minimum number of button presses for all machines`() {
        // Given the easy input with multiple machines from file

        // When we run part 1
        val result = secondPart(inputEasyLines)

        // Then we get the sum of minimum presses for all machines
        // For the first machine: 10 presses
        // For the second machine: 12 presses
        // For the third machine: 11 presses
        // Total: 33 presses
        assertEquals(33, result)
    }

    @Test
    fun `We get the max difference between all the joltages to know the minimum number of button presses needed to get to the goal`() {
        // given
        val currentJoltageLevels = listOf(3, 5, 4, 7)
        val targetJoltageLevels = listOf(12,5,6,30)
        // when
        val result = heuristic(targetJoltageLevels,currentJoltageLevels)
        // then
        assertEquals(23,result)
    }
    
    @Test
    fun `We can parse joltage levels to lightDiagrams`(){
        //given
        val zeroJoltages = mutableListOf(0,0,0)
        val allJoltageAreActive = mutableListOf(1,1,1)
        val smallJoltageMachine = mutableListOf(2,3,4,5)
        val biggestJoltageMachine = mutableListOf(74,50,269,303)
        // when
        val zeroResult = zeroJoltages.toLightDiagram()
        val AllActiveResult = allJoltageAreActive.toLightDiagram()
        val smallResult = smallJoltageMachine.toLightDiagram()
        val biggestResult = biggestJoltageMachine.toLightDiagram()
        // then
        val allJoltageActiveExpectedResult = BitSet(3)
        allJoltageActiveExpectedResult.set(0)
        allJoltageActiveExpectedResult.set(1)
        allJoltageActiveExpectedResult.set(2)

        val smallExpectedResult = BitSet(4)
        smallExpectedResult.set(1)
        smallExpectedResult.set(3)
        
        val biggestExpectedResult = BitSet(4)
        biggestExpectedResult.set(2)
        biggestExpectedResult.set(3)

        assertEquals(BitSet(3),zeroResult)
        assertEquals(allJoltageActiveExpectedResult, AllActiveResult)
        assertEquals(smallExpectedResult, smallResult)
        assertEquals(biggestExpectedResult, biggestResult)
    }
    
    @Test
    fun `We can get the valid button combinations`(){
        // Given
        val inputLines = mutableListOf(
            "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}"
        )
        val specifiedJoltageLevels = parseJoltageLevels(inputLines).map{it.toMutableList()}
        val joltagesToLightDiagrams = specifiedJoltageLevels.map{it.toLightDiagram()}
        val joltageLevelSize = specifiedJoltageLevels[0].size
        val buttonsWiring = parseButtonsWiring(inputLines, joltageLevelSize).map{it.toList()}
        val amountOfMachines = inputLines.size
        
        // when
        var result = mutableSetOf<List<BitSet>>()
        for(i in 0 until amountOfMachines){
            result = getValidButtonsCombinations(buttonsWiring[i], joltagesToLightDiagrams[i], joltageLevelSize)
        }
        // then
        val button1 = BitSet(4).apply { set(3) }
        val button2 = BitSet(4).apply { set(1);set(3) }
        val button3 = BitSet(4).apply { set(2) }
        val button4 = BitSet(4).apply { set(2); set(3) }
        val button5 = BitSet(4).apply { set(0); set(2) }
        val button6 = BitSet(4).apply { set(0); set(1) }
        
        val expectedResult = mutableSetOf(
            listOf(button3,button4, button6), // [{2}, {2, 3}, {0, 1}]
            listOf(button2,button3, button5), // [{1, 3}, {2}, {0, 2}]
            listOf(button1,button6),          // [{3}, {0, 1}]
            listOf(button1,button2, button4, button5)  // [{3}, {1, 3}, {2, 3}, {0, 2}]
        )
        assertEquals(expectedResult,  result)
    }
    
    @Test
    fun `We don't get out of memory for complex machines`(){
        // Given
        val inputLines = mutableListOf(
            "[#.##...##] (0,2,3,5,7) (1,2,3,4,5,7,8) (1,6,8) (2,3,4,8) (0,1,2,3,6,8) (0,1,3,4,5,6,7) (1,2,3,5,7,8) (2,3,6,8) (1,2,3,4,5) (0,2,3,7) (0,3,4,5) {74,50,269,303,47,70,231,56,245}"
        )
        // When we get the ans of the second part
        val result = secondPart(inputLines)

        // Then we expect 100 pressed buttons and that we don't get an OutOfMemoryError
        val expected = 100

        assertEquals(expected, result)
    }
}
