package com.aoc.day03

import java.io.File

/**
 * Main entry point that reads input file and executes either part 1 or part 2 solution
 * based on user input (1 for part 1, any other number for part 2)
 */
fun main(){
    val fileName = "src/main/kotlin/day03/input.txt"
    val lines = File(fileName).readLines()
    val mode = readln().trim().toInt()
    if(mode == 1){
        firstPart(lines)
    }else{
        secondPart(lines)
    }
}

/**
 * Solution for part 1: For each line, find the maximum digit from left to right
 * and the maximum digit from right to left (after the position of maxLeft),
 * then combine them to form a two-digit number and sum all numbers.
 */
fun firstPart(lines: List<String>){
    var ans = 0L
    for(line in lines){
        // Find max digit scanning from left and its position
        val (maxLeft, posMaxLeft) = getMaxLeft(line,0, line.length - 1)
        // Find max digit scanning from right, starting after maxLeft's position
        val (maxRight, posMaxRight) = getMaxRight(line, line.length -1, posMaxLeft + 1)
        val number = "$maxLeft$maxRight"
        println("number: $number")
        ans += number.toLong()
    }
    println("Result part 1: $ans")
}

/**
 * Solution for part 2: For each line, find the 11 maximum digits from left to right
 * (each search starting after the position of the previous max),
 * then find 1 more max digit from right to left, combine all to form a 12-digit number,
 * and sum all numbers.
 */
fun secondPart(lines: List<String>){
    var ans = 0L
    for(line in lines){
        // Build a 12-digit number: 11 digits from left + 1 from right
        var leftStart = 0
        var number = ""

        // Find 11 max digits from left, each search starting after previous max
        for(i in 11 downTo 1){
            // The end boundary shrinks with each iteration to ensure we leave enough
            // characters for the remaining iterations
            val (maxLeft, posMaxLeft) = getMaxLeft(line, leftStart, line.length - i)
            // Update start position for next search to be after current max
            leftStart = posMaxLeft + 1
            number += maxLeft
        }

        // Find the last (12th) digit from the right side
        val (maxRight, posMaxRight) = getMaxRight(line, line.length -1, leftStart)
        number += maxRight
        println("number: $number")
        ans += number.toLong()
    }
    println("Result part 2: $ans")
}

/**
 * Finds the maximum digit in a string from left to right within a specified range.
 * 
 * @param line The string to search in
 * @param start The starting index (inclusive)
 * @param end The ending index (exclusive)
 * @return A Pair containing the maximum digit found and its position in the string
 */
fun getMaxLeft(line: String,start: Int = 0,end: Int = line.length): Pair<Char, Int>{
    var maxLeft = '0'
    var position= 0

    for(i in start until end){
        // Compare digit values by subtracting chars (works because digits are sequential in ASCII)
        if(line[i] - maxLeft > 0){
            maxLeft = line[i]
            position = i
            // Optimization: if we find a 9, it can't get any higher, so stop searching
            if(maxLeft >= '9') break
        }
    }
    return Pair(maxLeft, position)
}
/**
 * Finds the maximum digit in a string from right to left within a specified range.
 * 
 * @param line The string to search in
 * @param start The starting index (inclusive, from the right)
 * @param end The ending index (inclusive, from the right)
 * @return A Pair containing the maximum digit found and its position in the string
 */
fun getMaxRight(line: String,start: Int = 0,end: Int = line.length): Pair<Char, Int>{
    var maxRight = '0'
    var position= 0

    // Loop from start to end in reverse order (right to left)
    for(i in start downTo end){
        // Compare digit values by subtracting chars (works because digits are sequential in ASCII)
        if(line[i] - maxRight > 0){
            maxRight = line[i]
            position = i
            // Optimization: if we find a 9, it can't get any higher, so stop searching
            if(maxRight >= '9') break
        }
    }
    return Pair(maxRight, position)
}
