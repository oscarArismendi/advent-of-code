package com.aoc.day11

import java.io.File

/**
 * Main entry point for the Day 11 solution.
 * Reads input file, executes either part 1 or part 2 based on user input,
 * and prints the result along with execution time.
 */
fun main(){
    val fileName = "src/main/kotlin/day11/input.txt"
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
 * Solves part 2 of the puzzle.
 * Counts the number of paths from the starting node to "out" that pass through both "dac" and "fft" nodes.
 * Uses memoization to avoid recalculating paths for states that have been seen before.
 *
 * @param lines List of strings representing the graph connections
 * @return Total number of valid paths that contain both "dac" and "fft" nodes
 */
fun secondPart(lines: MutableList<String>): Long {
    val adjacencyList = lines.toAdjacencyList()
    val start = State("svr", false, false)
    val totalNumberOfPathsFromYouToOut = countOutPathsThatContainDacAndFft(start, adjacencyList, mutableMapOf())
    return totalNumberOfPathsFromYouToOut
}

/**
 * Solves part 1 of the puzzle.
 * Counts the total number of paths from "you" to "out" in the graph.
 *
 * @param lines List of strings representing the graph connections
 * @return Total number of paths from "you" to "out"
 */
fun firstPart(lines: MutableList<String>): Int {
    val adjacencyList = lines.toAdjacencyList()
    val totalNumberOfPathsFromYouToOut = countOutPaths(adjacencyList)
    return totalNumberOfPathsFromYouToOut
}

/**
 * Counts all possible paths from "you" to "out" in the graph.
 * Uses a depth-first search approach with a stack to explore all paths.
 *
 * @param adjacencyList Map representing the graph where keys are nodes and values are lists of connected nodes
 * @return Total number of paths from "you" to "out"
 */
fun countOutPaths(adjacencyList: Map<String, List<String>>): Int {
    var counter = 0
    val source = "you"
    // Go as much as it can in one direction
    // Stack, last in first out
    val stack = ArrayDeque(listOf(source))
    while (stack.isNotEmpty()) {
        val current = stack.removeLastOrNull()
        println(current)
        if (current == "out") {
            counter++
            continue
        }
        if (adjacencyList[current] == null) continue
        adjacencyList[current]!!.map {
            stack.add(it)
        }
    }
    return counter
}


/**
 * Represents the state of a path traversal in the graph.
 * Tracks the current node and whether specific nodes have been visited.
 *
 * @property node The current node in the path
 * @property seenDac Whether the path has visited the "dac" node
 * @property seenFft Whether the path has visited the "fft" node
 */
data class State(val node: String, val seenDac: Boolean = false, val seenFft: Boolean = false)

/**
 * Counts all paths from the current state to "out" that pass through both "dac" and "fft" nodes.
 * Uses dynamic programming with memoization to avoid recalculating paths for states that have been seen before.
 *
 * @param state Current state of the path traversal
 * @param graph Map representing the graph where keys are nodes and values are lists of connected nodes
 * @param memo Memoization cache to store already computed results for each state
 * @return Number of valid paths from the current state to "out" that pass through both "dac" and "fft"
 */
fun countOutPathsThatContainDacAndFft(
    state: State,
    graph: Map<String, List<String>>,
    memo: MutableMap<State, Long>
): Long {
    // Base case: reached the end node
    if (state.node == "out") {
        return if (state.seenDac && state.seenFft) 1 else 0
    }

    // Return memoized result if available
    memo[state]?.let { return it }

    var sum = 0L
    for (next in graph[state.node].orEmpty()) {
        val nextState = State(
            node = next,
            seenDac = state.seenDac || next == "dac",
            seenFft = state.seenFft || next == "fft"
        )
        sum += countOutPathsThatContainDacAndFft(nextState, graph, memo)
    }

    // Cache the result before returning
    memo[state] = sum
    return sum
}

/**
 * Extension function that converts a list of strings to an adjacency list representation of a graph.
 * Each string in the list should be in the format "source: destination1 destination2 ...".
 *
 * @return Map where keys are source nodes and values are lists of destination nodes
 */
fun MutableList<String>.toAdjacencyList(): Map<String, List<String>> {
    val adjacencyList = mutableMapOf<String, MutableList<String>>()
    for (line in this) {
        val (source, destinations) = line.split(":")
        destinations.trim().split(" ").map {
            adjacencyList.getOrPut(source) { mutableListOf() }.add(it)
        }
    }
    return adjacencyList.toMap()
}
