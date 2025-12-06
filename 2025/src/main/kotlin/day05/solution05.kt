package com.aoc.day05

import java.io.File
import java.util.SortedSet
import kotlin.math.max

/**
 * Main function that reads input file, processes it based on user selection (mode 1 or 2),
 * and measures execution time.
 */
fun main(){
    val fileName = "src/main/kotlin/day05/input.txt"
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
 * Solves the first part of the problem - counts fresh ingredients.
 * First reads range definitions, then checks each ingredient against these ranges.
 * An ingredient is fresh if it falls within any of the defined ranges.
 */
fun firstPart(lines: List<String>){
    var readingRanges = true
    val lowerBoundSet = sortedSetOf<Long>()
    val upperBoundMap = mutableMapOf<Long, Long>()
    var ans = 0
    for(line in lines){
        if(line.isEmpty()){
            readingRanges = false
            continue
        }
        if(readingRanges){
            addRange(line, upperBoundMap, lowerBoundSet)
        }else{
            val ingredientId = line.toLong()

            if(isFresh(lowerBoundSet, ingredientId, upperBoundMap)) ans++
        }
    }
    println("First part answer: $ans")
}

/**
 * Determines if an ingredient is fresh by checking if its ID falls within any defined range.
 * 
 * @param lowerBoundSet Sorted set of lower bounds of all ranges
 * @param ingredientId ID of the ingredient to check
 * @param upperBoundMap Map of lower bounds to their corresponding upper bounds
 * @return true if the ingredient is fresh (within any range), false otherwise
 */
private fun isFresh(
    lowerBoundSet: SortedSet<Long>,
    ingredientId: Long,
    upperBoundMap: MutableMap<Long, Long>
): Boolean {
    for (lower in lowerBoundSet) {
        if (lower > ingredientId) break  // Optimization: stop if we've passed the ingredient ID
        if (upperBoundMap.getOrDefault(lower, 0) >= ingredientId) {
            return true  // Ingredient is within this range
        }
    }
    return false
}

/**
 * Parses a range definition from a line and adds it to the data structures.
 * Format of line is expected to be "lower-upper" where both are numbers.
 * 
 * @param line String containing the range definition
 * @param upperBoundMap Map storing upper bounds for each lower bound
 * @param lowerBoundSet Sorted set of all lower bounds for efficient traversal
 */
private fun addRange(
    line: String,
    upperBoundMap: MutableMap<Long, Long>,
    lowerBoundSet: SortedSet<Long>
) {
    val (lower, upper) = line.split("-").map { it.toLong() }
    val currentBoundMapValue = upperBoundMap.getOrDefault(lower,0)
    if (currentBoundMapValue == 0L) lowerBoundSet.add(lower)  // Only add to set if this is a new lower bound
    upperBoundMap[lower] = max(upperBoundMap.getOrDefault(lower, 0), upper)  // Use max to handle overlapping ranges
}

/**
 * Solves the second part of the problem - calculates the total size of all ranges
 * after merging overlapping ranges.
 * 
 * 1. Reads all range definitions
 * 2. Merges overlapping ranges using meshRanges
 * 3. Calculates the sum of the sizes of all merged ranges
 */
fun secondPart(lines: List<String>){
    val lowerBoundSet = sortedSetOf<Long>()
    val upperBoundMap = mutableMapOf<Long, Long>()
    var ans = 0L
    for(line in lines){
        if(line.isEmpty()){
            break
        }
        addRange(line, upperBoundMap, lowerBoundSet)
    }
    meshRanges(lowerBoundSet, upperBoundMap)  // Merge overlapping ranges
//    for(lower in lowerBoundSet){
//        println("$lower -> ${upperBoundMap.getOrDefault(lower, 0)}")
//    }
    // Calculate total size of all ranges (upper - lower + 1 for each range)
    for(lower in lowerBoundSet){
        ans += (upperBoundMap.getOrDefault(lower, 0L) - lower + 1L)
    }
    println("Second part answer: $ans")
}

/**
 * Merges overlapping ranges in a single pass through the sorted lower bounds.
 * 
 * The algorithm works by:
 * 1. Iterating through lower bounds in ascending order
 * 2. For each pair of consecutive ranges, checking if they overlap
 * 3. If they overlap, merging them by extending the first range and removing the second
 * 4. If they don't overlap, moving to the next pair
 * 
 * @param lowerBoundSet Sorted set of lower bounds of all ranges
 * @param upperBoundMap Map of lower bounds to their corresponding upper bounds
 */
private fun meshRanges(
    lowerBoundSet: SortedSet<Long>,
    upperBoundMap: MutableMap<Long, Long>
) {
    var lastLower = -1L  // Track the lower bound of the previous range
    val lowerToRemove = mutableSetOf<Long>()  // Collect bounds to remove after iteration

    for (currentLower in lowerBoundSet) {
        if (lastLower == -1L) {
            lastLower = currentLower  // Initialize with the first lower bound
            continue
        }

        val lastUpper = upperBoundMap.getOrDefault(lastLower, 0)
        if (lastUpper >= currentLower) {
            // Ranges overlap: last range's upper bound >= current range's lower bound
            val currentUpper = upperBoundMap.getOrDefault(currentLower, 0)
            // Extend the previous range to include the current range
            upperBoundMap[lastLower] = max(lastUpper, currentUpper)
            upperBoundMap.remove(currentLower)  // Remove the merged range
            lowerToRemove.add(currentLower)  // Mark for removal from the set
        } else {
            // Ranges don't overlap, move to the next pair
            lastLower = currentLower
        }
    }

    // Remove all merged ranges from the set
    for (lower in lowerToRemove) {
        lowerBoundSet.remove(lower)
    }
}
