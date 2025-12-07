package com.aoc.day06

import java.io.File

/**
 * Main function that reads input file, processes it based on user selection (mode 1 or 2),
 * and measures execution time.
 */
fun main(){
    val fileName = "src/main/kotlin/day06/input.txt"
    val lines = File(fileName).readLines().toMutableList()
    val mode = readln().trim().toInt() // Read user input to determine which part to run
    val startTime = System.currentTimeMillis()
    if(mode == 1){
        firstPart(lines)
    }else{
        secondPart(lines)
    }

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}

/**
 * Solves the first part of the problem - processes a grid of numbers horizontally.
 * Each column in the grid becomes a separate problem to solve.
 * When an operator line (+ or *) is encountered, it applies the operations to each column.
 * 
 * @param lines Input lines containing numbers and operators
 */
fun firstPart(lines: List<String>){
    val problems =  mutableListOf< MutableList<Long>>()
    var ans = 0L
    for(i in 0..lines.size - 1){
        val stringLine = lines[i].trim()
        if(stringLine[0] == '+' || stringLine[0] == '*'){
            // Found operator line - apply operations to each column
            val line = stringLine.split("\\s+".toRegex())
            for(index in 0 until problems.size){
                val total = operateLine(line[index], problems[index])
                ans += total
            }
            break
        }
        addLineToProblems(stringLine, problems)
    }
    println("ans: $ans")
}

/**
 * Performs either addition or multiplication on a list of numbers.
 * 
 * @param operator The operation to perform ("+" for addition, "*" for multiplication)
 * @param problem List of numbers to operate on
 * @return The result of applying the operation to all numbers in the list
 */
fun operateLine(
    operator: String,
    problem: MutableList<Long>
): Long {
    var total = 0L
    if (operator == "+") {
        for (number in problem) {
            total += number
        }
    } else {
        // For multiplication, start with 1 as the identity element
        total = 1L
        for (number in problem) {
            total *= number
        }
    }
    return total
}

/**
 * Parses a line of space-separated numbers and adds each number to its corresponding column in the problems grid.
 * This function transforms horizontal rows into vertical columns for processing.
 * 
 * @param stringLine A string containing space-separated numbers
 * @param problems The data structure to store the parsed numbers, organized by column
 */
fun addLineToProblems(
    stringLine: String,
    problems: MutableList<MutableList<Long>>,
) {
    // Split by any number of spaces and convert to Long values
    val line = stringLine.split("\\s+".toRegex()).map { it.toLong() }

    for (i in 0 until line.size) {
        // Create a new column if needed
        if(problems.getOrNull(i) == null) problems.add(mutableListOf())
        // Add the number to its corresponding column
        problems[i].add(line[i])
    }
}

/**
 * Similar to addLineToProblems but keeps values as strings instead of converting to Long.
 * Used for part 2 where preserving the original string format (including spaces) is important.
 * 
 * @param stringLine A string containing space-separated values
 * @param problems The data structure to store the parsed strings, organized by column
 */
fun addLineToProblemsAsString(
    stringLine: String,
    problems: MutableList<MutableList<String>>,
) {
    // Split by any number of spaces but keep as strings
    val line = stringLine.split("\\s+".toRegex())

    for (i in 0 until line.size) {
        // Create a new column if needed
        if(problems.getOrNull(i) == null) problems.add(mutableListOf())
        // Add the string to its corresponding column
        problems[i].add(line[i])
    }
}

/**
 * Solves the second part of the problem - processes the grid using a different approach.
 * This part reads the grid from right to left, processing numbers in a special "cephalopod notation"
 * where numbers are read vertically rather than horizontally.
 * 
 * @param lines Input lines containing numbers and operators
 */
fun secondPart(lines: List<String>){
    val problems =  mutableListOf< MutableList<String>>()
    var ans = 0L
    val problemWithSpaces = mutableListOf<String>() // Collects numbers for current operation
    var i = 0
    var savePoint = 10000 // Tracks position in line to resume processing

    while(i < lines.size){
        val stringLine = lines[i]
        // Initialize savePoint to end of line on first iteration
        if(savePoint == 10000) savePoint = stringLine.length - 1
        val stringLineLastIndex = savePoint
        var isLastANumber = false
        var number = ""

        // Process the line from right to left
        for(stringLineIndex in stringLineLastIndex downTo 0){
            // If we've collected a complete number (ended by a space)
            if(isLastANumber && stringLine[stringLineIndex] == ' ' ) {
                problemWithSpaces.add(number)
                break
            }

            // If we found addition operator
            if(stringLine[stringLineIndex] == '+' ){
                // Convert collected numbers to vertical notation and calculate
                val problemInVerticalNotation = convertProblemToVerticalNotation(problemWithSpaces)
                val total = operateStringLine("+", problemInVerticalNotation)
                ans += total
                problemWithSpaces.clear()

                // If not at beginning of line, reset to process previous part
                if(stringLineIndex != 0) {
                    savePoint = stringLineIndex - 2 // Move left of the operator
                    i = -1 // Reset line counter to start over
                }
                break
            }

            // If we found multiplication operator
            if(stringLine[stringLineIndex] == '*' ){
                // Convert collected numbers to vertical notation and calculate
                val problemInVerticalNotation = convertProblemToVerticalNotation(problemWithSpaces)
                val total = operateStringLine("*", problemInVerticalNotation)
                ans += total
                problemWithSpaces.clear()

                // If not at beginning of line, reset to process previous part
                if(stringLineIndex != 0) {
                    savePoint = stringLineIndex - 2 // Move left of the operator
                    i = -1 // Reset line counter to start over
                }
                break
            }

            // Build number from right to left
            number = "${stringLine[stringLineIndex]}${number}"

            // If we reached the beginning of line with a number, add it
            if(stringLineIndex == 0 && (stringLine[stringLineIndex] != '+' && stringLine[stringLineIndex] != '*')) 
                problemWithSpaces.add(number)

            isLastANumber = stringLine[stringLineIndex].isDigit()
        }
        i++
    }
    println("ans: $ans")
}

/**
 * Converts a list of numbers from horizontal to vertical notation (cephalopod notation).
 * This transformation reads digits from right to left and stacks them vertically.
 * For example, ["123", "45"] becomes ["35", "24", "1"] (read right-to-left, then bottom-to-top).
 * 
 * @param problem List of number strings to convert
 * @return List of strings representing the numbers in vertical notation
 */
fun convertProblemToVerticalNotation(problem: MutableList<String>): MutableList<String>{
    val problemInVerticalNotation = mutableListOf<String>()

    for(number in problem){
        var verticalIndex = 0
        // Process each digit from right to left
        for(numberIndex in number.length - 1 downTo 0){
            // Create a new vertical position if needed
            if(problemInVerticalNotation.getOrNull(verticalIndex) == null){
                problemInVerticalNotation.add("")
            }

            // Skip spaces, but increment vertical index to maintain alignment
            if(number[numberIndex] == ' '){
                verticalIndex++
                continue
            }

            // Add the digit to its vertical position
            problemInVerticalNotation[verticalIndex] = "${problemInVerticalNotation[verticalIndex]}${number[numberIndex]}"
            verticalIndex++
        }
    }
    return problemInVerticalNotation
}

/**
 * Applies the vertical notation conversion to all problems in the grid.
 * This is a utility function that applies convertProblemToVerticalNotation to each column.
 * 
 * @param problems The grid of problems to convert, organized by column
 */
fun convertAllProblemsToVerticalNotation(problems: MutableList<MutableList<String>>){
    for(index in 0 until problems.size){
        problems[index] = convertProblemToVerticalNotation(problems[index])
    }
}

/**
 * Similar to operateLine but works with string representations of numbers.
 * Performs either addition or multiplication on a list of number strings.
 * 
 * @param operator The operation to perform ("+" for addition, "*" for multiplication)
 * @param problem List of number strings to operate on
 * @return The result of applying the operation to all numbers in the list
 */
fun operateStringLine(
    operator: String,
    problem: MutableList<String>
): Long {
    var total = 0L
    if (operator == "+") {
        for (number in problem) {
            total += number.toLong()
        }
    } else {
        // For multiplication, start with 1 as the identity element
        total = 1L
        for (number in problem) {
            total *= number.toLong()
        }
    }
    return total
}
