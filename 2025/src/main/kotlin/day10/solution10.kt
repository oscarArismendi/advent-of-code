package com.aoc.day10

import java.io.File
import java.util.BitSet

/**
 * Main entry point for the Day 10 solution.
 * Reads input file, executes either part 1 or part 2 based on user input,
 * and prints the result along with execution time.
 */
fun main(){
    val fileName = "src/main/kotlin/day10/input.txt"
    val lines = File(fileName).readLines().toMutableList()
    val mode = readln().trim().toInt() // Read user input to determine which part to run
    val startTime = System.currentTimeMillis()
    val ans = if(mode == 1){
        firstPart(lines)
    }else{
        secondPart(lines)
    }

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
    println("ans: $ans")
}

/**
 * Solves part 1 of the puzzle.
 * Parses light diagrams and button wirings from input, then finds the minimum
 * number of button presses needed for each machine and returns the total.
 *
 * @param lines List of strings representing light diagrams and button wirings
 * @return Total minimum number of button presses needed for all machines
 */
fun firstPart(lines: MutableList<String>): Int{
    val lightDiagrams = parseLightDiagram(lines)
    val lightDiagramSize = lightDiagrams[0].size()
    val buttonsWiring = parseButtonsWiring(lines, lightDiagramSize)
    val amountOfMachines = lines.size

    var total = 0

    for(i in 0 until amountOfMachines){
        val minimumPresses = findMinimumNumberOfPresses(buttonsWiring[i],lightDiagrams[i])
        total += minimumPresses
    }
    return total
}

/**
 * Placeholder for part 2 solution.
 */
fun secondPart(lines: MutableList<String>){}

/**
 * Parses light diagrams from input lines.
 * Each light diagram is enclosed in square brackets, e.g., [.##.].
 * Converts each diagram to a BitSet where '#' represents a lit light.
 *
 * @param lines List of strings containing light diagrams
 * @return List of BitSets representing light diagrams
 */
fun parseLightDiagram(lines: MutableList<String>): List<BitSet>{
    val lightDiagrams = mutableListOf<BitSet>()
    val amountOfLines = lines.size
    for(row in 0 until amountOfLines){
        val positionOfClosingDiagram = lines[row].indexOf("]")
        val stringOfLightDiagram = lines[row].substring(1, positionOfClosingDiagram) // Extract content between [ and ]
        val currentLightDiagram = stringOfLightDiagram.toBitSet()
        lightDiagrams.add(currentLightDiagram)
    }
    return lightDiagrams
}

/**
 * Parses button wirings from input lines.
 * Each button wiring is enclosed in parentheses, e.g., (1,3).
 * Converts each wiring to a BitSet where the numbers represent the positions affected.
 *
 * @param lines List of strings containing button wirings
 * @param lightDiagramSize Size of the light diagram to ensure consistent BitSet sizes
 * @return Nested list of BitSets representing button wirings for each machine
 */
fun parseButtonsWiring(lines: MutableList<String>,lightDiagramSize: Int): MutableList<MutableList<BitSet>>{
    val amountOfLines = lines.size
    val buttonsWiring = MutableList(amountOfLines) { mutableListOf<BitSet>() }
    for(row in 0 until amountOfLines){
        var startIndex = 0
        while(true){
            val positionOfOpeningParentheses = lines[row].indexOf("(",startIndex)
            if(positionOfOpeningParentheses == -1) break // Exit loop if no more parentheses found
            val positionOfClosingParentheses = lines[row].indexOf(")",positionOfOpeningParentheses)
            val stringOfButtonWiring = lines[row].substring(positionOfOpeningParentheses + 1, positionOfClosingParentheses)
            val currentButtonWiring = stringOfButtonWiring.toBitSet(lightDiagramSize)
            buttonsWiring[row].add(currentButtonWiring)
            startIndex = positionOfClosingParentheses + 1 // Move past this parenthesis for next iteration
        }
    }
    return buttonsWiring
}

/**
 * Finds the minimum number of button presses needed to achieve the target light diagram.
 * Tries increasing numbers of presses until a solution is found.
 *
 * @param buttonsWiring List of BitSets representing button wirings
 * @param lightDiagram BitSet representing the target light diagram
 * @return Minimum number of presses needed, or -1 if impossible
 */
fun findMinimumNumberOfPresses(buttonsWiring: MutableList<BitSet>, lightDiagram: BitSet): Int {
    for (presses in 1..buttonsWiring.size) {
        if (canAchieveWithPresses(buttonsWiring, lightDiagram, presses)) {
            return presses
        }
    }
    return -1 // Return -1 if no solution found (should not happen with valid input)
}

/**
 * Recursive function to check if the target light diagram can be achieved with a given number of presses.
 * Uses backtracking to explore all possible combinations of button presses.
 *
 * @param buttons List of BitSets representing button wirings
 * @param target BitSet representing the target light diagram
 * @param pressesLeft Number of button presses remaining
 * @param currentState Current state of the light diagram
 * @param startIndex Index to start from (to avoid using the same button twice)
 * @return True if target can be achieved, false otherwise
 */
private fun canAchieveWithPresses(
    buttons: MutableList<BitSet>,
    target: BitSet,
    pressesLeft: Int,
    currentState: BitSet = BitSet(target.size()),
    startIndex: Int = 0
): Boolean {
    if (pressesLeft == 0) {
        return currentState == target // Base case: check if we've reached the target
    }

    for (i in startIndex until buttons.size) {
        val nextState = currentState.clone() as BitSet
        nextState.xor(buttons[i]) // Apply the button press using XOR operation

        if (canAchieveWithPresses(buttons, target, pressesLeft - 1, nextState, i + 1)) {
            return true
        }
    }

    return false // No solution found with this combination
}

/**
 * Extension function to convert a string to a BitSet.
 * Handles different formats:
 * - '#' characters set the bit at the corresponding index
 * - Numbers set the bit at the value of the number
 *
 * @param size Optional size for the BitSet (defaults to string length)
 * @return BitSet representation of the string
 */
private fun String.toBitSet(size: Int = 0): BitSet {
    val sizeOfTheBitSet = if(size > 0) size else this.length
    val response = BitSet(sizeOfTheBitSet)
    for(i in this.indices){
        if(this[i] == '#') response[i] = true // For light diagram format: '#' means light is on
        else if(this[i] != '.' && this[i] != ',') response[this[i].toString().toInt()] = true // For button wiring format: numbers represent positions
    }
    return response
}
