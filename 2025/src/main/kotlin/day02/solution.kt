package com.aoc.day02

import java.io.File

fun main() {
    val fileName = "src/main/kotlin/day02/input.txt"
    val lines = File(fileName).readLines()
    val line = lines[0]
//    println("Result: $line")
    val mode = readln().trim().toInt()
    if(mode == 1){
        firstPart(line)
    }else{
        secondPart(line)
    }
}

// Solves part 2 of the problem:
// "an ID is invalid if it is made only of some sequence of digits repeated at least twice"
//
// This function works by:
// 1. Iterating through each range of IDs
// 2. For each range, trying all possible partition values from 2 up to the length of the upper bound
// 3. For each partition value, finding all invalid IDs in the range
// 4. Adding up all the invalid IDs

fun secondPart(line: String) : Long {
    var ans = 0.toLong()
    val ranges = line.split(",")
    for(range in ranges){
        val (lower,upper) = getLowerAndUpperBound(range)
        // Keep track of added numbers to avoid counting the same number multiple times
        val addedNumbers = mutableSetOf<Long>()

        // Iterate through all numbers in the range
        for (num in lower..upper) {
            val numStr = num.toString()
            val numLength = numStr.length

            // Check all possible pattern lengths from 1 to half the length of the number
            for (patternLength in 1..(numLength / 2)) {
                // Only check if the number length is divisible by the pattern length
                if (numLength % patternLength == 0) {
                    val partition = numLength / patternLength
                    if (partition >= 2 && isIdInvalid(numStr, partition)) {
                        if (!addedNumbers.contains(num)) {
                            ans += num
                            addedNumbers.add(num)
//                            println("Ans: $ans (Added: $num)")
                        }
                        break  // Once we've found an invalid pattern, no need to check others
                    }
                }
            }
        }
    }
    println(ans)
    return ans
}

// Solves part 1 of the problem:
// "an ID is invalid if it is made only of some sequence of digits repeated twice"
//
// This function works by:
// 1. Iterating through each range of IDs
// 2. For each range, finding all invalid IDs with partition=2 (i.e., a sequence repeated exactly twice)
// 3. Adding up all the invalid IDs
//
// Unlike part 2, we only need to check for partition=2 in part 1.
fun firstPart(line: String) {
    var ans = 0.toLong()
    val ranges = line.split(",")
    for(range in ranges){
        val (lower,upper) = getLowerAndUpperBound(range)
        var start = lower

        while(start <= upper){
            // Check if the current number is invalid with partition=2
            if(isIdInvalid(start.toString()) ){
                ans += start
//                println("Ans: $ans (Added: $start)")
            }
            // Find the next invalid ID with partition=2
            start = getNextInvalidId(start.toString())
//            println("Next invalid id: $start")
        }
    }
    println(ans)
}

// Checks if an ID is invalid according to the problem statement:
// "an ID is invalid if it is made only of some sequence of digits repeated at least twice"
//
// This function works by:
// 1. Checking if the ID can be divided into exactly 'partition' equal parts
// 2. Checking if all parts are identical
//
// By trying all possible partition values from 2 up to the length of the ID,
// we can identify all invalid IDs according to the problem statement.
fun isIdInvalid(id : String, partition: Int = 2): Boolean{
    // If the ID length is not divisible by the partition, it can't be divided into equal parts
    if(id.length % partition != 0) return false
    // Single-digit IDs can't be invalid
    if(id.length == 1) return false
    // Divide the ID into 'partition' equal parts
    val listOfPartitions = id.chunked(id.length/partition)
    val length = listOfPartitions.size
    // Check if all parts are identical
    for(i in 0..length-2){
        val firstPartition = listOfPartitions[i].toLong()
        val secondPartition = listOfPartitions[i+1].toLong()
//        println("comparing $firstPartition | $secondPartition")
        if(firstPartition != secondPartition) return false
    }
//    println("")
    return true
}

// Parses a range string in the format "lower-upper" and returns a pair of Long values
// representing the lower and upper bounds of the range.
//
// For example, "11-22" returns Pair(11, 22)
fun getLowerAndUpperBound(range: String): Pair<Long, Long>{
//    println("get lower and upper bound for $range")
    val arrayOfRange = range.split("-")
    val lowerBound = arrayOfRange[0].toLong()
    val upperBound = arrayOfRange[1].toLong()
//    println("Lower Bound: $lowerBound | Upper Bound: $upperBound")
    return Pair(lowerBound, upperBound)
}

fun getNextInvalidId(id: String, partition: Int = 2): Long{
//    println("get next invalid id for $id")
    val length = id.length
    val idValue = id.toLong()
    // Case 1: If the length is not divisible by the partition, find the next ID with a length that is
    if(length % partition != 0){
        // Find the smallest ID with a length divisible by partition that is greater than the current ID
        val minLength = ((length / partition) + 1) * partition
        val minPattern = "1" + "0".repeat(minLength / partition - 1)
        val minId = minPattern.repeat(partition).toLong()

        if (minId > idValue) {
            return minId
        }
    }
    // Case 2: Check for a pattern repeated exactly 'partition' times
    val listOfIdNumbersDividedIntoPartitions = mutableListOf<String>()
    val sizeOfEachPartitions = length / partition

    // Split the ID into 'partition' equal parts
    for(i in  0 until partition){
        val startIndex= i*sizeOfEachPartitions
        val endIndex = startIndex + sizeOfEachPartitions
        listOfIdNumbersDividedIntoPartitions.add(id.substring(startIndex, endIndex))
    }

    // Check if all patterns are the same
    val allSame = listOfIdNumbersDividedIntoPartitions.all { it == listOfIdNumbersDividedIntoPartitions[0] }

    if (allSame) {
        // If all patterns are the same, increment the pattern and repeat
        val pattern = listOfIdNumbersDividedIntoPartitions[0]
        val nextPattern = (pattern.toLong() + 1).toString()
        return nextPattern.repeat(partition).toLong()
    }
    // Case 3: Find the next ID that consists of a pattern repeated exactly 'partition' times
    // First, check if we can create a valid ID by repeating the first pattern
    val firstPartition = listOfIdNumbersDividedIntoPartitions[0]
    val repeatedFirstPartition = firstPartition.repeat(partition).toLong()
    if(repeatedFirstPartition > idValue){
        return repeatedFirstPartition
    }

    // If not, increment the first pattern and repeat
    val nextPartition = firstPartition.toLong().plus(1)
    return nextPartition.toString().repeat(partition).toLong()
}
