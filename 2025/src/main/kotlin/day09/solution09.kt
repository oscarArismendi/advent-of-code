package com.aoc.day09

import java.io.File
import kotlin.math.abs

/**
 * Main entry point for the Day 9 solution.
 * Reads input file, executes either part 1 or part 2 based on user input,
 * and prints the result along with execution time.
 */
fun main(){
    val fileName = "src/main/kotlin/day09/input.txt"
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
 * Creates a list of areas between all coordinate pairs and returns the largest area.
 *
 * @param lines List of coordinate strings in format "x,y"
 * @return The largest area between any two coordinates
 */
fun firstPart(lines: MutableList<String>): Long{
    val areasList = createAListOfAreas(lines)
    return areasList[0] // Return the largest area (first element in descending sorted list)
}

/**
 * Placeholder for part 2 solution.
 */
fun secondPart(lines: MutableList<String>){}

/**
 * Calculates the area of a rectangle formed by two coordinates.
 * The formula accounts for inclusive boundaries by adding 1 to each dimension.
 *
 * @param firstCoordinate First coordinate as a Pair<Long,Long> (x,y)
 * @param secondCoordinate Second coordinate as a Pair<Long,Long> (x,y)
 * @return Area of the rectangle formed by the two coordinates
 */
fun calculateRectangleAreaBetweenTwoCoordinates(firstCoordinate: Pair<Long,Long>,secondCoordinate: Pair<Long,Long>): Long{
    val(x1,y1) = firstCoordinate
    val(x2,y2) = secondCoordinate
    return (abs(x2 - x1) + 1) * (abs(y2 - y1) + 1) // +1 to include both endpoints in the area calculation
}

/**
 * Creates a list of areas between all possible pairs of coordinates.
 * Processes each pair of coordinates exactly once and sorts the resulting areas in descending order.
 *
 * @param lines List of coordinate strings in format "x,y"
 * @return List of areas sorted in descending order
 */
fun createAListOfAreas(lines: MutableList<String>): List<Long>{
    val areas = mutableListOf<Long>()
    val coordinateIterations = lines.size - 1
    for(first in 0 until coordinateIterations ){
        for(second in first+1 .. coordinateIterations){
            // Parse coordinates from strings like "x,y" to Pair<Long,Long>
            val firstCoordinatePair = lines[first].split(",").map { it.toLong() }.toPair()
            val secondCoordinatePair = lines[second].split(",").map { it.toLong() }.toPair()

            val area = calculateRectangleAreaBetweenTwoCoordinates(firstCoordinatePair, secondCoordinatePair)
            areas.add(area)
        }
    }
    return areas.sortedDescending() // Sort areas in descending order to have largest first
}

/**
 * Extension function to convert a List<Long> to a Pair<Long,Long>.
 * Throws an exception if the list doesn't contain exactly two elements.
 *
 * @return Pair created from the first two elements of the list
 * @throws IllegalArgumentException if the list size is not 2
 */
private fun List<Long>.toPair(): Pair<Long,Long> {
    if(this.size != 2) throw IllegalArgumentException("List must contain exactly two elements")
    return this[0] to this[1]
}
