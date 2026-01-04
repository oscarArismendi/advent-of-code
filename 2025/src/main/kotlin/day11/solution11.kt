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

fun secondPart(lines: MutableList<String>) {
    TODO("Not yet implemented")
}

fun firstPart(lines: MutableList<String>): Int {
    val adjacencyList = lines.toAdjacencyList()
    val totalNumberOfPathsFromYouToOut = countOutPaths(adjacencyList)
    return totalNumberOfPathsFromYouToOut
}

fun countOutPaths(adjacencyList: Map<String, List<String>>): Int {
    var counter = 0
    val source = "you"
    // Go as much as it can in one direction
    // Stack, last in first out
    val stack = ArrayDeque(listOf(source))
    while (stack.isNotEmpty()) {
        val current = stack.removeLastOrNull()
        println(current)
        if(current == "out"){
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

fun MutableList<String>.toAdjacencyList(): Map<String, List<String>> {
    val adjacencyList = mutableMapOf<String, MutableList<String>>()
    for(line in this){
        val (source, destinations) = line.split(":")
        destinations.trim().split(" ").map{
            adjacencyList.getOrPut(source) { mutableListOf() }.add(it)
        }
    }
    return adjacencyList.toMap()
}




