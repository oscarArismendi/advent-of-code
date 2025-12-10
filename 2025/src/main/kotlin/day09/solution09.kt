package com.aoc.day09

import java.io.File
import kotlin.math.abs

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

fun firstPart(lines: MutableList<String>): Long{
    val areasList = createAListOfAreas(lines)
    return areasList[0]
}

fun secondPart(lines: MutableList<String>){}

fun calculateRectangleAreaBetweenTwoCoordinates(firstCoordinate: Pair<Long,Long>,secondCoordinate: Pair<Long,Long>): Long{
    val(x1,y1) = firstCoordinate
    val(x2,y2) = secondCoordinate
    return (abs(x2 - x1) + 1) * (abs(y2 - y1) + 1)
}

fun createAListOfAreas(lines: MutableList<String>): List<Long>{
    val areas = mutableListOf<Long>()
    val coordinateIterations = lines.size - 1
    for(first in 0 until coordinateIterations ){
        for(second in first+1 .. coordinateIterations){
            val firstCoordinatePair = lines[first].split(",").map { it.toLong() }.toPair()
            val secondCoordinatePair = lines[second].split(",").map { it.toLong() }.toPair()
            
            val area = calculateRectangleAreaBetweenTwoCoordinates(firstCoordinatePair, secondCoordinatePair)
            areas.add(area)
        }
    }
    return areas.sortedDescending()
}

private fun List<Long>.toPair(): Pair<Long,Long> {
    if(this.size != 2) throw IllegalArgumentException("List must contain exactly two elements")
    return this[0] to this[1]
}

