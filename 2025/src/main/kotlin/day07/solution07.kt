package com.aoc.day07

import java.io.File

/**
 * Main function that reads input file, processes it based on user selection (mode 1 or 2),
 * and measures execution time.
 */
fun main(){
    val fileName = "src/main/kotlin/day07/input.txt"
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
 * Solves the first part of the problem - counts the total number of tachyon splits
 * by recursively traversing the grid starting from the 'S' position.
 * 
 * @param lines The input grid representing tachyon manifolds
 * @return The total number of tachyon splits encountered
 */
fun firstPart(lines: MutableList<String>): Int{
    val row = 0
    val column = lines[row].indexOf('S')  // Find the starting position marked with 'S'
    val ans = countTachyonSplits(lines, 0, Pair(row, column))
    println(ans)
    return ans
}

/**
 * Solves the second part of the problem - calculates the total number of timelines
 * by processing the grid iteratively and tracking timeline counts.
 * 
 * @param lines The input grid representing tachyon manifolds
 * @return The total number of timelines created
 */
fun secondPart(lines: MutableList<String>): Long{
    val row = 0
    val column = lines[row].indexOf('S')
    val mutableLines = lines.map { StringBuilder(it) }.toMutableList()
    val ans = prepareTachyonTimelines(mutableLines,0, Pair(row,column))
    println(ans)
    return ans
}

/**
 * Recursively counts the number of tachyon splits in the manifold grid.
 * A split occurs when a tachyon beam encounters a '^' character, causing it to split into two paths.
 * The function marks the path with '|' characters to avoid counting the same path twice.
 * 
 * @param tachyonManifolds The grid representing tachyon manifolds
 * @param splits The current count of splits encountered so far
 * @param position The current position (row, column) in the grid
 * @return The total number of splits encountered in all paths
 */
fun countTachyonSplits(tachyonManifolds: MutableList<String>, splits: Int, position: Pair<Int, Int>): Int{
    val tachyonManifoldsSize = tachyonManifolds.size

    // Base case: reached the bottom of the grid
    if(tachyonManifoldsSize == position.first+1) return splits

    val currentPoint = tachyonManifolds[position.first][position.second]
    val nextPoint = tachyonManifolds[position.first+1][position.second]

    // Case 1: Encountered a split point ('^')
    if(nextPoint == '^'){
        val currentSplits = splits + 1  // Increment split count
        val nextLeftPoint = tachyonManifolds[position.first+1][position.second-1]
        val nextRightPoint = tachyonManifolds[position.first+1][position.second+1]

        // Mark left path if it's not already visited
        if(nextLeftPoint == '.'){
            tachyonManifolds[position.first+1] = tachyonManifolds[position.first + 1].updateCharAt(position.second-1,'|')
        }

        // Mark right path if it's not already visited
        if(nextRightPoint == '.'){
            tachyonManifolds[position.first+1] = tachyonManifolds[position.first + 1].updateCharAt(position.second+1,'|')
        }

        // Recursively explore left path
        val leftSide = countTachyonSplits(
            tachyonManifolds,
            0,
            Pair(position.first+1, position.second-1)
        )

        // Recursively explore right path
        val rightSide = countTachyonSplits(
            tachyonManifolds,
            0,
            Pair(position.first+1, position.second+1)
        )

        // Combine splits from both paths plus the current split
        return leftSide + rightSide + currentSplits
    }

    // Case 2: Encountered an already visited path
    if(nextPoint == '|'){
        return splits  // Stop exploration on this path
    }

    // Case 3: Continue on the current path
    // Mark the next position as visited
    tachyonManifolds[position.first+1] = tachyonManifolds[position.first + 1].updateCharAt(position.second,'|')

    // Continue recursively along the path
    return countTachyonSplits(
        tachyonManifolds,
        splits,
        Pair(position.first+1, position.second)
    )
}

/**
 * Calculates the total number of timelines by processing the tachyon manifold grid iteratively.
 * Instead of using recursion, this function processes the grid row by row, tracking the number
 * of timelines at each position using a separate map.
 * 
 * @param tachyonManifolds The grid representing tachyon manifolds, using StringBuilder for efficiency
 * @param splits Initial count of splits (usually 0)
 * @param position The starting position (row, column) in the grid
 * @return The total number of timelines created
 */
fun prepareTachyonTimelines(tachyonManifolds: MutableList<StringBuilder>, splits: Long, position: Pair<Int, Int>): Long{
    val tachyonManifoldsSize = tachyonManifolds.size
    val tachyonManifoldsRowLength = tachyonManifolds[0].length

    // Create a map to track the number of timelines at each position
    val timelineMap = MutableList(tachyonManifoldsSize){MutableList(tachyonManifoldsRowLength){0L}}

    // Process the grid row by row
    for(tachyonFoldRowIndex in 0 until tachyonManifoldsSize - 1){
        for(tachyonFoldColumnIndex in 0 until tachyonManifoldsRowLength){
            val currentTachyonPoint = tachyonManifolds[tachyonFoldRowIndex][tachyonFoldColumnIndex]
            val nextTachyonPoint = tachyonManifolds[tachyonFoldRowIndex+1][tachyonFoldColumnIndex]

            // Case 1: Starting point - initialize with 1 timeline
            if(currentTachyonPoint == 'S'){ 
                timelineMap[tachyonFoldRowIndex][tachyonFoldColumnIndex] = 1L
                tachyonManifolds[tachyonFoldRowIndex+1][tachyonFoldColumnIndex] = '|'
            }
            // Case 2: Path continuation - propagate timeline count from above
            else if(currentTachyonPoint == '|'){
                timelineMap[tachyonFoldRowIndex][tachyonFoldColumnIndex] += timelineMap[tachyonFoldRowIndex-1][tachyonFoldColumnIndex]
                if(nextTachyonPoint != '^'){
                    tachyonManifolds[tachyonFoldRowIndex][tachyonFoldColumnIndex] = '|'
                }
            }
            // Case 3: Split point - distribute timeline count to left and right
            else if(currentTachyonPoint == '^'){
                // Add the timeline count from above to both left and right positions
                timelineMap[tachyonFoldRowIndex][tachyonFoldColumnIndex-1] += timelineMap[tachyonFoldRowIndex-1][tachyonFoldColumnIndex]
                timelineMap[tachyonFoldRowIndex][tachyonFoldColumnIndex+1] += timelineMap[tachyonFoldRowIndex-1][tachyonFoldColumnIndex]

                // Mark both paths as visited
                tachyonManifolds[tachyonFoldRowIndex][tachyonFoldColumnIndex-1] = '|'
                tachyonManifolds[tachyonFoldRowIndex][tachyonFoldColumnIndex+1] = '|'
            }
            // Case 4: Empty space - check if it's reachable from above
            else if(currentTachyonPoint == '.'){
                if(tachyonFoldRowIndex == 0) continue  // Skip first row

                // If there's a path from above, propagate the timeline count
                if(tachyonManifolds[tachyonFoldRowIndex-1][tachyonFoldColumnIndex] == '|'){
                    timelineMap[tachyonFoldRowIndex][tachyonFoldColumnIndex] += timelineMap[tachyonFoldRowIndex-1][tachyonFoldColumnIndex]
                    tachyonManifolds[tachyonFoldRowIndex][tachyonFoldColumnIndex] = '|'
                }
            }
        }
    }

    // Sum all timeline counts from the second-to-last row to get the total
    return timelineMap[tachyonManifoldsSize-2].sum()
}

/**
 * Extension function to update a character at a specific index in a String.
 * Since Strings are immutable in Kotlin, this creates a new String with the updated character.
 * 
 * @param index The position of the character to update
 * @param newChar The new character to place at the specified index
 * @return A new String with the character at the specified index replaced
 */
fun String.updateCharAt(index: Int, newChar: Char): String {
    val chars = this.toCharArray()
    chars[index] = newChar
    return String(chars)
}

// Could have used it to avoid doing a timeline map, but I forgot about it :(
/**
 * Calculates the next timeline character in sequence.
 * This function converts characters to represent timeline numbers:
 * - '.' or 'S' becomes '1' (first timeline)
 * - '1' through '9' increment normally
 * - '9' becomes 'A' (representing 10)
 * - Letters 'A' through 'Y' represent values 10 through 34
 * 
 * @param currentChar The current timeline character
 * @return The next character in the timeline sequence
 */
//fun nextTimeline(currentChar: Char): Char{
//    if(currentChar == '.' || currentChar == 'S') return '1'  // Initialize to first timeline
//    if(currentChar == '9') return 'A'  // A represents 10, B represents 11, and so on
//    // Works for digits 0-8 and letters A to Y
//    return currentChar.code.plus(1).toChar()  // Increment to next character in sequence
//}
