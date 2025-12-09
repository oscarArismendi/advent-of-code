package com.aoc.day08

import com.aoc.utils.dataStructures.UnionFind
import java.io.File
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3
import kotlin.math.sqrt
import kotlin.text.split

//private const val THRESHOLD_FIRST_PART = 10 // easy
private const val THRESHOLD_FIRST_PART = 1000

/**
 * Main function that reads input file, processes it based on user selection (mode 1 or 2),
 * and measures execution time.
 */
fun main(){
    val fileName = "src/main/kotlin/day08/input.txt"
    val lines = File(fileName).readLines().toMutableList()
    val mode = readln().trim().toInt() // Read user input to determine which part to run
    val startTime = System.currentTimeMillis()
    val ans = if(mode == 1){
        firstPart(lines,THRESHOLD_FIRST_PART)
    }else{
        secondPart(lines)
    }

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
    println("ans: $ans")
}

/**
 * Solves the first part of the problem - connects points based on their Euclidean distance.
 * 
 * The algorithm:
 * 1. Creates a list of all possible point pairs sorted by distance
 * 2. Connects points in order of increasing distance until reaching the threshold
 * 3. Returns the product of the sizes of the three largest connected components
 * 
 * @param coordinates List of 3D coordinates in format "x,y,z"
 * @param threshold Maximum number of connections to make
 * @return Product of the sizes of the three largest connected components
 */
fun firstPart(coordinates: MutableList<String>, threshold: Int): Int{
    val unionFind = UnionFind(coordinates.size)  // Initialize UnionFind data structure
    val instructionList = createInstructionList(coordinates)  // Create sorted list of point pairs
    var connections = 0

    // Connect points in order of increasing distance
    for(instruction in instructionList){
        val (node1, node2) = instruction.second.split("-").map { it.toInt() }
        unionFind.union(node1, node2)  // Connect the two points
        connections++
        if(connections == threshold) break  // Stop after reaching the threshold
    }

    return unionFind.multiplyTheThreeLargestGroupSizes()  // Calculate the result
}

/**
 * Solves the second part of the problem - finds the product of x-coordinates of the last connection.
 * 
 * The algorithm:
 * 1. Creates a list of all possible point pairs sorted by distance
 * 2. Connects points in order of increasing distance until all points are in one component
 * 3. Identifies the last connection that unified all points
 * 4. Returns the product of the x-coordinates of the two points in that connection
 * 
 * @param coordinates List of 3D coordinates in format "x,y,z"
 * @return Product of x-coordinates of the last connection that unified all points
 */
fun secondPart(coordinates: MutableList<String>): Long{
    val unionFind = UnionFind(coordinates.size)  // Initialize UnionFind data structure
    val instructionList= createInstructionList(coordinates)  // Create sorted list of point pairs
    var connections = 0
    var lastUnion = Pair(-1,-1)  // Will store the indices of the last connection

    // Connect points in order of increasing distance
    for(instruction in instructionList){
        val (node1, node2) = instruction.second.split("-").map { it.toInt() }
        unionFind.union(node1, node2)  // Connect the two points
        connections++

        // Check if all points are now in one component
        if(unionFind.numberOfComponents == 1) {
            lastUnion = Pair(node1,node2)  // Store the last connection
            break
        }
    }

    // Extract coordinates of the two points in the last connection
    val (x1,y1,z1) = coordinates[lastUnion.first].split(",").map{it.toInt()}
    val (x2,y2,z2) = coordinates[lastUnion.second].split(",").map{it.toInt()}

    // Return the product of x-coordinates
    return x1.toLong() * x2.toLong()
}

/**
 * Creates a list of all possible point pairs sorted by their Euclidean distance.
 * 
 * This function:
 * 1. Generates all unique pairs of points (n choose 2 combinations)
 * 2. Calculates the Euclidean distance between each pair
 * 3. Returns a list of pairs sorted by increasing distance
 * 
 * @param coordinates List of 3D coordinates in format "x,y,z"
 * @return List of pairs (distance, "index1-index2") sorted by distance
 */
fun createInstructionList(coordinates:MutableList<String>): List<Pair<Double,String>>{
    val instructionList = mutableListOf<Pair<Double,String>>()
    val coordinatesIterations = coordinates.size - 1

    // Generate all unique pairs of points (O(n²) operation)
    for (first in 0 until coordinatesIterations){
        for(second in first+1..coordinatesIterations){
            // Calculate distance between the two points
            val distance = calculateEuclideanDistance(coordinates[first],coordinates[second])
            // Store the distance and the indices of the points
            instructionList.add(distance to "$first-${second}")
        }
    }

    // Sort the list by distance (ascending)
    return instructionList.sortedBy { it.first }
}

/**
 * Calculates the Euclidean distance between two 3D points.
 * 
 * The formula used is: sqrt((x₁-x₂)² + (y₁-y₂)² + (z₁-z₂)²)
 * 
 * @param firstPoint First 3D coordinate in format "x,y,z"
 * @param secondPoint Second 3D coordinate in format "x,y,z"
 * @return The Euclidean distance between the two points
 */
fun calculateEuclideanDistance(firstPoint: String,secondPoint: String): Double{
    // Parse the coordinates from the string format "x,y,z"
    val (currentX,currentY,currentZ) = firstPoint.split(",").map{it.toDouble()}
    val (nextX,nextY,nextZ) = secondPoint.split(",").map{it.toDouble()}

    // Calculate the squared differences for each dimension
    val x = (currentX-nextX).squared()
    val y = (currentY-nextY).squared()
    val z = (currentZ-nextZ).squared()

    // Return the square root of the sum of squared differences
    return sqrt(x + y + z)
}

/**
 * Extension function that squares a Double value.
 * 
 * @return The square of the Double value (value * value)
 */
private fun Double.squared(): Double {
    return this*this
}
