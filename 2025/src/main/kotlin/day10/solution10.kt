package com.aoc.day10

import java.io.File
import java.util.BitSet
import java.util.PriorityQueue
import kotlin.math.min

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
 * Solves part 2 of the puzzle.
 * Parses joltage levels and button wirings from input, then finds the minimum
 * number of button presses needed for each machine using a bifurcation approach.
 *
 * @param lines List of strings representing light diagrams, button wirings, and joltage levels
 * @return Total minimum number of button presses needed for all machines
 */
fun secondPart(lines: MutableList<String>): Int{
    val specifiedJoltageLevels = parseJoltageLevels(lines).map{it.toMutableList()}
    val joltageLevelSize = specifiedJoltageLevels[0].size
    val buttonsWiring = parseButtonsWiring(lines, joltageLevelSize)
    val amountOfMachines = lines.size

    var total = 0

    for(i in 0 until amountOfMachines){
        val minimumPresses = findMinimumNumberOfPressesByBifurcating(
            buttonsWiring[i],
            specifiedJoltageLevels[i],
            mutableMapOf(),
        )
        if(minimumPresses != null){
            total += minimumPresses
        }
        println("Machine ${i+1}: $minimumPresses presses - specifiedJoltageLevels: ${specifiedJoltageLevels[i]}")
    }
    return total
}

/**
 * Extension function that converts a list of integers to a BitSet representation of a light diagram.
 * Each odd integer in the list corresponds to a light that is ON (bit set to true).
 * Each even integer in the list corresponds to a light that is OFF (bit set to false).
 *
 * @return BitSet where each bit represents the state of a light (true = ON, false = OFF)
 */
fun List<Int>.toLightDiagram(): BitSet {
    val bits = BitSet(this.size)
    for (i in this.indices) {
        if (this[i] % 2 != 0) {
            bits.set(i) // odd → light ON
        }
    }
    return bits
}

/**
 * Finds all valid combinations of buttons that can be pressed to achieve a target light diagram.
 * Uses a backtracking approach to explore all possible combinations of buttons.
 *
 * @param buttons List of BitSets representing button wirings
 * @param target BitSet representing the target light diagram
 * @param size Size of the light diagram
 * @return Set of all valid button combinations that achieve the target
 */
fun getValidButtonsCombinations(
    buttons: List<BitSet>,
    target: BitSet,
    size: Int
): MutableSet<List<BitSet>> {
    val results = mutableSetOf<List<BitSet>>()

    /**
     * Recursive backtracking function to find all valid button combinations.
     *
     * @param idx Current button index being considered
     * @param current Current state of the light diagram
     * @param chosen List of buttons chosen so far
     */
    fun backtrack(
        idx: Int,
        current: BitSet,
        chosen: MutableList<BitSet>
    ) {
        // If we've considered all buttons
        if (idx == buttons.size) {
            if (current == target) {
                results.add(chosen.toList())
            }
            return
        }

        // OPTION 1: skip this button
        backtrack(idx + 1, current, chosen)

        // OPTION 2: take this button
        val next = current.clone() as BitSet
        next.xor(buttons[idx])
        chosen.add(buttons[idx])

        backtrack(idx + 1, next, chosen)

        chosen.removeAt(chosen.lastIndex) // backtrack
    }

    backtrack(
        idx = 0,
        current = BitSet(size),
        chosen = mutableListOf()
    )

    return results
}

/**
 * Finds the minimum number of button presses needed to achieve a specified joltage level using BFS.
 * Uses a breadth-first search approach to explore all possible button press combinations.
 *
 * @param buttons List of BitSets representing button wirings
 * @param specifiedJoltage Target joltage levels to achieve
 * @param currentJoltage Current joltage levels
 * @return Minimum number of button presses needed, or the last explored state's presses if target cannot be reached
 */
fun findMinimumNumberOfPressesForJoltageWithBFS(
    buttons: MutableList<BitSet>,
    specifiedJoltage: IntArray,
    currentJoltage: IntArray,
): Int {
    val visitedJoltages = mutableSetOf<Int>()
    var numberOfPresses = 0
    val stack = ArrayDeque(listOf(currentJoltage to numberOfPresses)) 

    while (stack.isNotEmpty()) {
        val (currentJoltage, presses) = stack.removeFirst()
        numberOfPresses = presses // we want to be able to get the presses outside the loop

        // Skip already visited states
        if (currentJoltage.contentHashCode() in visitedJoltages) continue

        // Return if we've reached the target
        if (currentJoltage.contentEquals(specifiedJoltage)) return presses

        visitedJoltages.add(currentJoltage.contentHashCode())

        // Try pressing each button
        for (button in buttons) { 
            var nextJoltage = currentJoltage.copyOf()

            // Apply button press effects to joltage levels
            for (buttonPosition in 0 until button.size()) {
               if (button.get(buttonPosition)) {
                   nextJoltage[buttonPosition]++

                   // If any joltage exceeds target, this path is invalid
                   if (nextJoltage[buttonPosition] > specifiedJoltage[buttonPosition]) {
                       nextJoltage = IntArray(0)
                       break
                   }
               }
            }

            // Add valid next states to the queue
            if (nextJoltage.isNotEmpty()) stack.add(nextJoltage to presses + 1) // we add one to the presses
        }
    }    

    return numberOfPresses 
}

/**
 * Finds the minimum number of button presses needed to achieve a specified joltage level using A* search.
 * Uses the A* algorithm with a heuristic function to efficiently explore the state space.
 *
 * @param buttons List of BitSets representing button wirings
 * @param specifiedJoltage Target joltage levels to achieve
 * @param currentJoltage Current joltage levels
 * @return Minimum number of button presses needed, or -1 if target cannot be reached
 */
fun findMinimumNumberOfPressesForJoltageWithAStar(
    buttons: MutableList<BitSet>,
    specifiedJoltage: List<Int>,
    currentJoltage: List<Int>,
): Int {
    // Map to track the best (lowest) cost to reach each state
    val bestCost = mutableMapOf<List<Int>,Int>()

    // Priority queue ordered by f(n) = g(n) + h(n) where:
    // - g(n) is the cost so far (presses)
    // - h(n) is the heuristic estimate of remaining cost (futureCost)
    val stack = PriorityQueue<Triple<List<Int>,Int,Int>>(compareBy { 
        it.second + it.third 
    })

    // Initialize with starting state
    stack.add(Triple(
        currentJoltage, 
        0, // at the beginning we haven't pressed a button
        heuristic(specifiedJoltage, currentJoltage))
    )  

    while (stack.isNotEmpty()) {
        val (currentJoltage, presses, futureCost) = stack.poll()
        val best = bestCost[currentJoltage]

        // Skip if we've found a better path to this state already
        if (best != null && presses > best) continue // is not worth exploring an inefficient path

        // Return if we've reached the target
        if (currentJoltage == specifiedJoltage) {
            return presses
        }

        // Try pressing each button
        for (button in buttons) {
            // Calculate next state after pressing this button
            val nextJoltage = addButtonToJoltage(button, currentJoltage, specifiedJoltage)
            val nextState = nextJoltage.toList()

            // Calculate the minimum number of presses to reach the next state
            val nextMinimumNumberOfPresses = min((bestCost[nextState] ?: Int.MAX_VALUE), presses + 1)

            // Skip if this path is less efficient than one we've already found
            if (presses + 1 > nextMinimumNumberOfPresses) continue

            // Update the best cost to reach this state
            bestCost[nextState] = nextMinimumNumberOfPresses

            // Add valid next states to the priority queue
            if (nextState.isNotEmpty()) stack.add(Triple(
                nextJoltage,
                nextMinimumNumberOfPresses,
                heuristic(specifiedJoltage, nextState)
            ))
        }
    }    

    return -1 // we didn't arrive at a correct answer 
}

/**
 * Adds the effect of pressing a button to the current joltage levels.
 * Increments joltage levels at positions where the button has bits set.
 * Returns an empty list if any joltage level exceeds the specified target.
 *
 * @param button BitSet representing which positions to increment
 * @param currentJoltage Current joltage levels
 * @param specifiedJoltage Target joltage levels (used as upper bounds)
 * @return Updated joltage levels after pressing the button, or empty list if invalid
 */
private fun addButtonToJoltage(
    button: BitSet,
    currentJoltage: List<Int>,
    specifiedJoltage: List<Int>
): MutableList<Int> {
    var nextJoltage = currentJoltage.toMutableList()
    for (buttonPosition in 0 until button.size()) {
        if (button.get(buttonPosition)) {
            nextJoltage[buttonPosition]++
            // If any joltage exceeds the target, this button press is invalid
            if (nextJoltage[buttonPosition] > specifiedJoltage[buttonPosition]) {
                nextJoltage = mutableListOf()
                break
            }
        }
    }
    return nextJoltage
}

/**
 * Subtracts the effect of a button from the current joltage levels.
 * Decrements joltage levels at positions where the button has bits set.
 * Returns null if any joltage level would become negative.
 *
 * @param button BitSet representing which positions to decrement
 * @param currentJoltage Current joltage levels (can be null)
 * @return Updated joltage levels after subtracting the button effect, or null if invalid
 */
fun subtractButtonFromJoltage(
    button: BitSet,
    currentJoltage: List<Int>?,
): List<Int>? {
    if (currentJoltage == null) return null // don't make sense to operate on null joltage levels

    val nextJoltage = currentJoltage.toMutableList()
    for (buttonPosition in 0 until nextJoltage.size) {
        if (button.get(buttonPosition)) {
            nextJoltage[buttonPosition]--
            // If any joltage becomes negative, this subtraction is invalid
            if (nextJoltage[buttonPosition] < 0) {
                return null
            }
        }
    }
    return nextJoltage.toList()
}

/**
 * Finds the minimum number of button presses needed to achieve a specified joltage level using a recursive bifurcation approach.
 * This function uses dynamic programming with memoization to avoid redundant calculations.
 * The approach works by:
 * 1. Finding valid button combinations that transform the current state to an even-valued state
 * 2. Dividing (bifurcating) that state by 2
 * 3. Recursively solving the smaller problem
 *
 * @param buttons List of BitSets representing button wirings
 * @param currentJoltage Current joltage levels to be reduced to zero
 * @param cache Memoization cache to store already computed results
 * @return Minimum number of button presses needed, or null if impossible
 */
fun findMinimumNumberOfPressesByBifurcating(
    buttons: List<BitSet>,
    currentJoltage: List<Int>,
    cache: MutableMap<List<Int>, Int>,
): Int? {
    var best = Int.MAX_VALUE
    val lowerBound = currentJoltage.max()
    val numberOfZeros = currentJoltage.count { it == 0 }

    // Base case: if all joltage levels are zero, we're done
    val totalJoltageLevels = currentJoltage.size
    if (numberOfZeros == totalJoltageLevels) return 0

    // Check if we've already computed this state
    if (cache[currentJoltage] != null) return cache.getOrDefault(currentJoltage, Int.MAX_VALUE)

    // Convert joltage levels to light diagram representation
    val lightDiagram = currentJoltage.toLightDiagram()

    // Find all valid button combinations that can achieve the current light diagram
    val validButtonsCombinations = getValidButtonsCombinations(buttons, lightDiagram, totalJoltageLevels)

    // Try each valid button combination
    for (buttonList in validButtonsCombinations) {
        var resultHolder: List<Int>? = currentJoltage

        // Apply all buttons in the combination
        for (button in buttonList) {
            resultHolder = subtractButtonFromJoltage(button, resultHolder)
        }

        // Skip invalid subtractions
        if (resultHolder == null) continue

        // Skip if any joltage level is odd (can't be halved)
        if (resultHolder.any { it % 2 != 0 }) continue

        // Bifurcate: divide all joltage levels by 2
        val half = resultHolder.map { it / 2 }

        // Recursively solve the smaller problem
        val bifurcationResult = findMinimumNumberOfPressesByBifurcating(buttons, half, cache)

        if (bifurcationResult != null) {
            // Calculate total cost: buttons pressed now + 2 * buttons needed for the halved state
            val cost = buttonList.size + 2 * bifurcationResult

            // Skip if cost is less than the lower bound (impossible)
            if (cost < lowerBound) continue

            // Update best cost if this is better
            best = min(best, cost)
        }
    }

    // Cache the result if we found a solution
    // Int.MAX_VALUE should never be taken into account, it means that we simply didn't find an answer
    if (best != Int.MAX_VALUE) cache[currentJoltage] = best

    return if (best == Int.MAX_VALUE) null else best
}

/**
 * Calculates a heuristic value for the A* search algorithm.
 * The heuristic is the maximum difference between any corresponding joltage levels.
 * This provides an admissible heuristic (never overestimates) for the A* algorithm.
 *
 * @param specifiedJoltage Target joltage levels to achieve
 * @param currentJoltage Current joltage levels
 * @return Maximum difference between any corresponding joltage levels
 */
fun heuristic(
    specifiedJoltage: List<Int>,
    currentJoltage: List<Int>
): Int {
    var maxDifference = 0
    for (i in currentJoltage.indices) {
        val currentDifference = specifiedJoltage[i] - currentJoltage[i]
        if (currentDifference > maxDifference) maxDifference = currentDifference       
    }
    return maxDifference
}

/**
 * Parses joltage levels from input lines.
 * Each line contains joltage levels enclosed in curly braces, e.g., {3,5,4,7}.
 * Extracts and converts these values to arrays of integers.
 *
 * @param lines List of strings containing joltage levels
 * @return List of integer arrays representing joltage levels for each machine
 */
fun parseJoltageLevels(lines: MutableList<String>): MutableList<IntArray>{
    val joltageLevels = mutableListOf<IntArray>()
    val amountOfLines = lines.size
    for (row in 0 until amountOfLines) {
        val positionOfClosingJoltageLevels = lines[row].length - 1
        val positionOfStartingJoltageLevels = lines[row].indexOf("{") + 1
        val stringOfJoltageLevels = lines[row].substring(positionOfStartingJoltageLevels, positionOfClosingJoltageLevels) // Extract content between { and }
        val currentJoltageLevels = stringOfJoltageLevels.split(",").map { it.trim().toInt() }.toMutableList()
        joltageLevels.add(currentJoltageLevels.toIntArray())
    }
    return joltageLevels
}


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
 * - '#' characters set the bit at the corresponding index (for light diagrams)
 * - Numbers set the bit at the value of the number (for button wirings)
 * - Ignores '.' and ',' characters
 *
 * @param size Optional size for the BitSet (defaults to string length)
 * @return BitSet representation of the string
 */
private fun String.toBitSet(size: Int = 0): BitSet {
    val sizeOfTheBitSet = if (size > 0) size else this.length
    val response = BitSet(sizeOfTheBitSet)

    for (i in this.indices) {
        if (this[i] == '#') {
            response[i] = true // For light diagram format: '#' means light is on
        } else if (this[i] != '.' && this[i] != ',') {
            response[this[i].toString().toInt()] = true // For button wiring format: numbers represent positions
        }
    }

    return response
}
