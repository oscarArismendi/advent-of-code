package com.aoc.day09

import com.aoc.utils.algorithms.rayCasting.Ray2D
import com.aoc.utils.geometry.Point2D
import com.aoc.utils.geometry.Polygon2D
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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
 * Solution for part 2.
 * Finds the largest valid rectangle area formed by any two points in the polygon.
 * For inputEasy.txt, the expected answer is 24L (rectangle formed by points (2,5) and (9,7)).
 *
 * @param lines List of coordinate strings in format "x,y"
 * @return The largest valid rectangle area
 */
fun secondPart(lines: MutableList<String>): Long{

    val coordinates = mutableListOf<Point2D>()
    for(line in lines){
        val (x,y) = line.split(",").map { it.toDouble() }
        coordinates.add(Point2D(x,y))
    }
    val polygon = Polygon2D(coordinates)
    var ans = 0L

    println("Polygon has ${coordinates.size} vertices")

    for(first in coordinates.indices){
        for(second in first + 1 until coordinates.size){
            val firstCoordinate = coordinates[first].toLongPair()
            val secondCoordinate = coordinates[second].toLongPair()
            val newArea = calculateRectangleAreaBetweenTwoCoordinates(firstCoordinate, secondCoordinate)
            if(newArea > ans){
                val rectanglePoints: List<Point2D> = getRectanglePoints(coordinates[first], coordinates[second])
                val isValid = isRectangleValid(rectanglePoints, polygon)
                println("Checking rectangle: ${coordinates[first]} and ${coordinates[second]}")
                println("  Area: $newArea")
                println("  Number of points checked: ${rectanglePoints.size}")
                println("  Is valid: $isValid")
                if(isValid) {
                    ans = newArea
                    println("  New max area: $ans")
                }
            }
         }
    }
    println("Final answer: $ans")
    return ans
}

fun getRectanglePoints(
    firstCoordinate: Point2D,
    secondCoordinate: Point2D
): List<Point2D> {
    val (x1,y1) = firstCoordinate
    val (x2,y2) = secondCoordinate

    // If the two points form a line (same x or y), we need to check if all points on the line are inside the polygon
    if(x1 == x2 || y1 == y2){
        // For a horizontal line (same y)
        if(y1 == y2) {
            val minX = minOf(x1, x2)
            val maxX = maxOf(x1, x2)
            val points = mutableListOf<Point2D>()
            // Sample points along the line
            for(x in minX.toInt()..maxX.toInt()) {
                points.add(Point2D(x.toDouble(), y1))
            }
            return points
        }
        // For a vertical line (same x)
        else {
            val minY = minOf(y1, y2)
            val maxY = maxOf(y1, y2)
            val points = mutableListOf<Point2D>()
            // Sample points along the line
            for(y in minY.toInt()..maxY.toInt()) {
                points.add(Point2D(x1, y.toDouble()))
            }
            return points
        }
    }

    // For a rectangle, we need to check not just the corners but also points along the edges and inside
    val minX = minOf(x1, x2).toInt()
    val maxX = maxOf(x1, x2).toInt()
    val minY = minOf(y1, y2).toInt()
    val maxY = maxOf(y1, y2).toInt()

    val points = mutableListOf<Point2D>()

    // Add the four corners
    points.add(Point2D(minX.toDouble(), minY.toDouble()))
    points.add(Point2D(minX.toDouble(), maxY.toDouble()))
    points.add(Point2D(maxX.toDouble(), maxY.toDouble()))
    points.add(Point2D(maxX.toDouble(), minY.toDouble()))

    // Add points along the edges, but sample at intervals for large rectangles
    val width = maxX - minX
    val height = maxY - minY

    // For large rectangles, sample points at intervals to avoid OutOfMemoryError
    val sampleInterval = max(1, min(width, height) / 100)

    // Sample points along horizontal edges
    for (x in minX + sampleInterval until maxX step sampleInterval) {
        points.add(Point2D(x.toDouble(), minY.toDouble())) // Bottom edge
        points.add(Point2D(x.toDouble(), maxY.toDouble())) // Top edge
    }

    // Sample points along vertical edges
    for (y in minY + sampleInterval until maxY step sampleInterval) {
        points.add(Point2D(minX.toDouble(), y.toDouble())) // Left edge
        points.add(Point2D(maxX.toDouble(), y.toDouble())) // Right edge
    }

    // We don't need to check interior points, only edges are sufficient
    // Removing interior point checking to avoid OutOfMemoryError

    return points
}

fun isRectangleValid(listOfPoints: List<Point2D>,polygon: Polygon2D): Boolean{
    if(listOfPoints.isEmpty()) {
        return true
    }

    for(point in listOfPoints){
        val ray = Ray2D(point)
        val isInside = ray.cast(polygon)

        if(!isInside) {
            return false
        } 
    }
    return true // All points must be inside
}

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
